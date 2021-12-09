package bas.leanback.layout

import android.animation.*
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import bas.leanback.core.CallByOwner
import bas.leanback.core.loge
import com.bas.core.converter.toJson

/**
 * Created by Lucio on 2021/12/9.
 */
class EffectLayoutDelegate(
    val layout: ViewGroup,
    val callback: Callback,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) {
    companion object {

        @JvmStatic
        fun create(
            layout: ViewGroup,
            callback: Callback,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0
        ): EffectLayoutDelegate {
            return EffectLayoutDelegate(layout, callback, attrs, defStyleAttr)
        }

        private fun createMarginAdjuster(params: EffectParams, layout: ViewGroup): AbstractMarginAdjuster {
            if (layout is ConstraintLayout) {
                return ConstraintMarginAdjuster(params, layout)
            } else if(layout is RelativeLayout){
                return RelativeAdjuster(params, layout)
            }else {
                return MarginAdjuster(params, layout)
            }
        }
    }

    private var params: EffectParams

    private val shimmerPath: Path = Path()
    private val shimmerPaint: Paint = Paint()
    private var shimmerLinearGradient: LinearGradient? = null
    private var shimmerGradientMatrix: Matrix = Matrix()

    //shimmer 当前偏移量
    private var shimmerTranslate = 0f
    private val shimmerAnimator: Animator = ShimmerAnimator()

    //shimmer 动画启动标志
    private var isShimmerTranslating: Boolean = false

    private var refreshRectF: RectF = RectF()
    private var dispatchDrawFlag = false
    private var effectView: EffectView? = null

    private val frameRectF: RectF = RectF()

    //用于在启动动画前检测当前布局
    private var startAnimationPreDrawListener: ViewTreeObserver.OnPreDrawListener? = null

    private var animOnFocus: Animator? = null
    private var animOnUnfocus: Animator? = null

    private val marginAdjuster: AbstractMarginAdjuster

    init {
        layout.setWillNotDraw(false)
        params = EffectParams(layout.context, attrs)
        marginAdjuster = createMarginAdjuster(params, layout)
        logd(params.toJson().orEmpty())
    }

    //是否使用焦点动画
    private val focusAnimEnabled get() = params.scaleEnabled || params.shimmerEnabled

    private fun logd(msg: String) {
        Log.d("EffectLayout@${this.hashCode()}", msg)
    }

    @CallByOwner
    fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        logd("onSizeChanged($w,$h,$oldw,$oldh)")
        if (w == oldw && h == oldh) {
            logd("onSizeChanged 未发生改变")
            return
        }

        if (effectView == null) {
            //等布局大小确定之后，添加同等大小的效果View：实现阴影、边框、呼吸灯
            effectView = EffectView(params, layout.context)
            layout.addView(effectView, FrameLayout.LayoutParams(w, h))
            logd("创建Effect View并添加")
        } else {
            val effectViewIndex = layout.indexOfChild(effectView)
            if (effectViewIndex < 0) {
                logd("未添加Effect View，重新添加")
                (effectView!!.parent as? ViewGroup)?.removeView(effectView)
                layout.addView(effectView, FrameLayout.LayoutParams(w, h))
            } else {
                if (effectView?.width != w && effectView?.height != h) {
                    effectView?.updateLayoutParams<ViewGroup.LayoutParams> {
                        this.width = w
                        this.height = h
                    }
                    logd("Effect Size不同，修正")
                }
                if (effectViewIndex != layout.childCount - 1) {
                    logd("Effect View未在末尾，bringToFront")
                    effectView?.bringToFront()
                }
            }
        }
        updateShimmerParamsOnSizeChanged(w, h, oldw, oldh)
        if ((h != oldw || h != oldh) && params.isRoundedShape) {
            refreshRectF.set(
                layout.paddingLeft.toFloat(),
                layout.paddingTop.toFloat(),
                (w - layout.paddingRight).toFloat(),
                (h - layout.paddingBottom).toFloat()
            )
        }
    }

    @CallByOwner
    fun dispatchDraw(canvas: Canvas) {
        if (dispatchDrawFlag || !params.isRoundedShape) {
            callback.callSuperDispatchDraw(canvas)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.saveLayer(refreshRectF, null)
            } else {
                canvas.saveLayer(refreshRectF, null, Canvas.ALL_SAVE_FLAG)
            }
            callback.callSuperDispatchDraw(canvas)
            canvas.restore()
        }
        drawShimmer(canvas)
    }

    @CallByOwner
    fun draw(canvas: Canvas) {
        if (!params.isRoundedShape) {
            callback.callSuperDraw(canvas)
        } else {
            dispatchDrawFlag = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.saveLayer(refreshRectF, null)
            } else {
                canvas.saveLayer(refreshRectF, null, Canvas.ALL_SAVE_FLAG)
            }
            callback.callSuperDraw(canvas)
            canvas.restore()
        }
    }

    @CallByOwner
    fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        layout.isSelected = gainFocus
        if (gainFocus) {
            onGainFocus()
        } else {
            stopAnimation()
        }
    }

    @CallByOwner
    fun onViewAdded(child: View?) {
        child?.let {
            adjustChildMargin(it)
        }
    }

    @CallByOwner
    fun onDetachedFromWindow() {
        logd("onDetachedFromWindow")
        stopAnimation()
        callback.callSuperOnDetachedFromWindow()
    }

    private fun updateShimmerParamsOnSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        if (!params.shimmerEnabled)
            return

        val newLeft = layout.paddingLeft + params.shadowWidth + params.strokeWidth
        val newTop = layout.paddingTop + params.shadowWidth + params.strokeWidth
        val newRight = width - layout.paddingRight - params.shadowWidth - params.strokeWidth
        val newBottom = height - layout.paddingBottom - params.shadowWidth - params.strokeWidth
        //rectf 没有发生变化。直接返回
        if (newLeft == frameRectF.left && newTop == frameRectF.top && newRight == frameRectF.right && newBottom == frameRectF.bottom)
            return

        shimmerPath.reset()
        frameRectF.set(newLeft, newTop, newRight, newBottom)

        //必须使用这种方式，否则在某些情况下会出现中间有个阴影色的色块
        if (params.isRoundedShape) {
            shimmerPath.addRoundRect(frameRectF, params.cornerRadius, Path.Direction.CW)
        } else {
            shimmerPath.addRoundRect(frameRectF, 0f, 0f, Path.Direction.CW)
        }

        shimmerLinearGradient = LinearGradient(
            0f,
            0f,
            frameRectF.width(),
            frameRectF.height(),
            intArrayOf(
                0x00FFFFFF,
                reduceColorAlphaValueToZero(params.shimmerColor),
                params.shimmerColor,
                reduceColorAlphaValueToZero(params.shimmerColor),
                0x00FFFFFF
            ),
            floatArrayOf(0f, 0.2f, 0.5f, 0.8f, 1f),
            Shader.TileMode.CLAMP
        )
        shimmerPaint.shader = shimmerLinearGradient

        val screenWidth = layout.resources.displayMetrics.widthPixels
        val max = if (width >= height) width else height
        val duration = if (max > screenWidth / 3) screenWidth / 3 else max
        shimmerAnimator.duration = (duration * 3).toLong()
        shimmerAnimator.startDelay = params.shimmerDelay
    }

    private fun ShimmerAnimator(): Animator {
        return ValueAnimator.ofFloat(-1f, 1f).apply {
            interpolator = DecelerateInterpolator(1f)
            addUpdateListener {
                try {
                    if (!params.shimmerEnabled) {
                        it.cancel()
                        return@addUpdateListener
                    }
                    val value = it.animatedValue as Float
                    setShimmerTranslate(value)
                } catch (e: Exception) {
                    e.printStackTrace()
                    loge("[EffectLayout]:Shimmer动画异常", e)
                }
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    isShimmerTranslating = true
                }

                override fun onAnimationEnd(animation: Animator) {
                    isShimmerTranslating = false
                }

                override fun onAnimationCancel(animation: Animator?) {
                    isShimmerTranslating = false
                }
            })
        }
    }

    private fun setShimmerTranslate(translate: Float) {
        if (params.shimmerEnabled && this.shimmerTranslate != translate) {
            this.shimmerTranslate = translate
            ViewCompat.postInvalidateOnAnimation(layout)
        }
    }

    private fun drawShimmer(canvas: Canvas) {
        //如果没有启用闪光动画，或者闪光动画没有开始，则不绘制
        if (!params.shimmerEnabled || !isShimmerTranslating)
            return
        canvas.save()
        val shimmerTranslateX = frameRectF.width() * this.shimmerTranslate
        val shimmerTranslateY = frameRectF.height() * this.shimmerTranslate
        shimmerGradientMatrix.setTranslate(shimmerTranslateX, shimmerTranslateY)
        shimmerLinearGradient?.setLocalMatrix(shimmerGradientMatrix)
        canvas.drawPath(shimmerPath, shimmerPaint)
        canvas.restore()
    }

    private fun reduceColorAlphaValueToZero(actualColor: Int): Int {
        return Color.argb(
            0x1A,
            Color.red(actualColor),
            Color.green(actualColor),
            Color.blue(actualColor)
        )
    }

    private fun startAnimationOnFocused() {
        if (layout.width == 0) {
            startAnimationPreDrawListener = ViewTreeObserver.OnPreDrawListener {
                clearPreDrawListener()
                startAnimationOnFocused()
                true
            }
            layout.viewTreeObserver.addOnPreDrawListener(startAnimationPreDrawListener)
            return
        }
        startScaleAndShimmerAnim()
        effectView?.start()
    }

    private fun startScaleAndShimmerAnim() {
        if (!focusAnimEnabled) {
            animOnFocus?.end()
            return
        } else {
            animOnFocus?.cancel()
        }
        if (animOnFocus == null) {
            createFocusAnimator()?.let {
                animOnFocus = it
                it.start()
            }
        } else {
            animOnFocus?.start()
        }
    }

    private fun stopScaleAndShimmerAnim() {
        if (!focusAnimEnabled) {
            animOnUnfocus?.end()
            return
        } else {
            animOnUnfocus?.cancel()
        }

        if (animOnUnfocus == null) {
            createUnfocusAnimator()?.let {
                animOnUnfocus = it
                it.start()
            }
        } else {
            animOnUnfocus?.start()
        }
    }

    private fun createUnfocusAnimator(): Animator? {
        if (!params.scaleEnabled)
            return null
        return AnimatorSet().also {
            it.playTogether(
                getScaleXAnimator(1f),
                getScaleYAnimator(1f)
            )
        }
    }

    private fun createFocusAnimator(): Animator? {
        if (params.scaleEnabled && params.shimmerEnabled) {
            return AnimatorSet().also {
                it.playTogether(
                    getScaleXAnimator(params.scaleFactor),
                    getScaleYAnimator(params.scaleFactor)
                )
                it.playSequentially(shimmerAnimator)
            }
        } else if (params.scaleEnabled) {
            return AnimatorSet().also {
                it.playTogether(
                    getScaleXAnimator(params.scaleFactor),
                    getScaleYAnimator(params.scaleFactor)
                )
            }
        } else if (params.shimmerEnabled) {
            return shimmerAnimator
        } else {
            return null
        }
    }

    private fun clearPreDrawListener() {
        if (startAnimationPreDrawListener != null) {
            layout.viewTreeObserver.removeOnPreDrawListener(startAnimationPreDrawListener)
        }
        startAnimationPreDrawListener = null
    }

    private fun stopAnimation() {
        clearPreDrawListener()
        stopScaleAndShimmerAnim()
        effectView?.stop()
    }

    private fun getScaleXAnimator(scale: Float): ObjectAnimator {
        val scaleXObjectAnimator =
            ObjectAnimator.ofFloat(layout, "scaleX", scale)
                .setDuration(params.scaleAnimDuration)
        if (params.useBounceOnScale) {
            scaleXObjectAnimator.interpolator = BounceInterpolator()
        }
        return scaleXObjectAnimator
    }

    private fun getScaleYAnimator(scale: Float): ObjectAnimator {
        val scaleYObjectAnimator =
            ObjectAnimator.ofFloat(layout, "scaleY", scale)
                .setDuration(params.scaleAnimDuration)
        if (params.useBounceOnScale) {
            scaleYObjectAnimator.interpolator = BounceInterpolator()
        }
        return scaleYObjectAnimator
    }

    //获取焦点
    private fun onGainFocus() {
        if (params.bringToFrontOnFocus) {
            layout.bringToFront()
        }
        ensureEffectViewOnGainFocus()
        startAnimationOnFocused()
    }

    private fun ensureEffectViewOnGainFocus() {
        //没有effect效果，不处理
        if (params.strokeWidth <= 0 && params.shadowWidth <= 0)
            return
        val effectView = this.effectView

        val effectViewIndex = layout.indexOfChild(effectView)
        if (effectViewIndex < 0) {
            logd("未添加Effect View，重新添加")
            (effectView!!.parent as? ViewGroup)?.removeView(effectView)
            layout.addView(effectView, FrameLayout.LayoutParams(layout.width, layout.height))
        } else {
            if (effectView?.width != layout.width && effectView?.height != layout.height) {
                effectView?.updateLayoutParams<ViewGroup.LayoutParams> {
                    this.width = layout.width
                    this.height = layout.height
                }
                logd("Effect Size不同，修正")
            }
            if (effectViewIndex != layout.childCount - 1) {
                logd("Effect View未在末尾，bringToFront")
                effectView?.bringToFront()
            }
        }
    }

    private fun adjustChildMargin(child: View) {
        if (!params.adjustChildrenMargin || child == effectView)
            return

        marginAdjuster.adjustChildMargin(child)
//        val adjustMargin =
//            (params.strokeWidth + params.shadowWidth + params.childrenOffsetMargin).toInt()
//        if (adjustMargin <= 0)//不需要调整
//            return
//
//        val hasAdjusted = child.getTag(params.hashCode()) as? Boolean ?: false
//        if (hasAdjusted)//已调整过
//            return
//
//
//        val lp = child.layoutParams as? ViewGroup.MarginLayoutParams ?: return
//        child.updateLayoutParams<ViewGroup.MarginLayoutParams> {
//            lp.leftMargin += adjustMargin
//            lp.topMargin += adjustMargin
//            lp.rightMargin += adjustMargin
//            lp.bottomMargin += adjustMargin
//            //增加调整标记
//            child.setTag(params.hashCode(), true)
//        }
    }

    interface Callback {

        fun callSuperDispatchDraw(canvas: Canvas)

        fun callSuperDraw(canvas: Canvas)

        fun callSuperOnDetachedFromWindow()
    }


    internal abstract class AbstractMarginAdjuster(val params: EffectParams, val layout: ViewGroup) {
        abstract fun adjustChildMargin(child: View)
    }

    internal class MarginAdjuster(params: EffectParams, layout: ViewGroup) :
        AbstractMarginAdjuster(params, layout) {
        override fun adjustChildMargin(child: View) {

            val adjustMargin =
                (params.strokeWidth + params.shadowWidth + params.childrenOffsetMargin).toInt()
            if (adjustMargin <= 0)//不需要调整
                return

            val hasAdjusted = child.getTag(params.hashCode()) as? Boolean ?: false
            if (hasAdjusted)//已调整过
                return


            val lp = child.layoutParams as? ViewGroup.MarginLayoutParams ?: return
            child.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                lp.leftMargin += adjustMargin
                lp.topMargin += adjustMargin
                lp.rightMargin += adjustMargin
                lp.bottomMargin += adjustMargin
                //增加调整标记
                child.setTag(params.hashCode(), true)
            }
        }
    }

    internal class RelativeAdjuster(params: EffectParams, layout: ViewGroup) :
        AbstractMarginAdjuster(params, layout) {
        override fun adjustChildMargin(child: View) {

            val adjustMargin =
                (params.strokeWidth + params.shadowWidth + params.childrenOffsetMargin).toInt()
            if (adjustMargin <= 0)//不需要调整
                return

            val hasAdjusted = child.getTag(params.hashCode()) as? Boolean ?: false
            if (hasAdjusted)//已调整过
                return


            val lp = child.layoutParams as? RelativeLayout.LayoutParams ?: return
            child.updateLayoutParams<RelativeLayout.LayoutParams> {
                //暂时粗暴调整
                lp.leftMargin += adjustMargin
                lp.topMargin += adjustMargin
                lp.rightMargin += adjustMargin
                lp.bottomMargin += adjustMargin
                //增加调整标记
                child.setTag(params.hashCode(), true)
            }
        }
    }

    internal class ConstraintMarginAdjuster(params: EffectParams, layout: ViewGroup) :
        AbstractMarginAdjuster(params, layout) {
        override fun adjustChildMargin(child: View) {
            val adjustMargin =
                (params.strokeWidth + params.shadowWidth + params.childrenOffsetMargin).toInt()
            if (adjustMargin <= 0)//不需要调整
                return

            val hasAdjusted = child.getTag(params.hashCode()) as? Boolean ?: false
            if (hasAdjusted)//已调整过
                return

            val lp = child.layoutParams as? ConstraintLayout.LayoutParams ?: return
            child.updateLayoutParams<ConstraintLayout.LayoutParams> {
                if (leftToLeft == ConstraintLayout.LayoutParams.PARENT_ID) {
                    lp.leftMargin += adjustMargin
                    lp.goneLeftMargin += adjustMargin
                }

                if (topToTop == ConstraintLayout.LayoutParams.PARENT_ID) {
                    lp.topMargin += adjustMargin
                    lp.goneTopMargin += adjustMargin
                }

                if (rightToRight == ConstraintLayout.LayoutParams.PARENT_ID) {
                    lp.rightMargin += adjustMargin
                    lp.goneRightMargin += adjustMargin
                }

                if (bottomToBottom == ConstraintLayout.LayoutParams.PARENT_ID) {
                    lp.bottomMargin += adjustMargin
                    lp.goneBottomMargin += adjustMargin
                }
                //增加调整标记
                child.setTag(params.hashCode(), true)
            }
        }
    }
}