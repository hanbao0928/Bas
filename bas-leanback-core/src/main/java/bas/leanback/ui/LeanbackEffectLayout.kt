package bas.leanback.ui

import android.animation.*
import android.content.Context
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
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import bas.leanback.core.loge
import com.bas.core.converter.toJson
import java.util.*


class LeanbackEffectLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

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
    private var effectView: LeanbackEffectView? = null

    private val frameRectF: RectF = RectF()

    //用于在启动动画前检测当前布局
    private var startAnimationPreDrawListener: ViewTreeObserver.OnPreDrawListener? = null

    private var animOnFocus: Animator? = null
    private var animOnUnfocus: Animator? = null

    init {
        setWillNotDraw(false)
        params = EffectParams(context, attrs)
        logd(params.toJson().orEmpty())
    }

    override fun isInEditMode(): Boolean {
        return true
    }

    private fun logd(msg: String) {
        Log.d("EffectLayout@${this.hashCode()}", msg)
    }


    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(width, height, oldw, oldh)
        logd("onSizeChanged($width,$height,$oldw,$oldh)")

        if (width == oldw && height == oldh) {
            logd("onSizeChanged 未发生改变")

            return
        }

        if (effectView == null) {
            //等布局大小确定之后，添加同等大小的效果View：实现阴影、边框、呼吸灯
            effectView = LeanbackEffectView(params, context)
            addView(effectView, LayoutParams(width, height))
            logd("创建Effect View并添加")
        } else {
            val effectViewIndex = indexOfChild(effectView)
            if (effectViewIndex < 0) {
                logd("未添加Effect View，重新添加")
                (effectView!!.parent as? ViewGroup)?.removeView(effectView)
                addView(effectView, LayoutParams(width, height))
            } else {
                if (effectView?.width != width && effectView?.height != height) {
                    effectView?.updateLayoutParams<ViewGroup.LayoutParams> {
                        this.width = width
                        this.height = height
                    }
                    logd("Effect Size不同，修正")
                }
                if (effectViewIndex != childCount - 1) {
                    logd("Effect View未在末尾，bringToFront")
                    effectView?.bringToFront()
                }
            }
        }
        updateShimmerParamsOnSizeChanged(width, height, oldw, oldh)
        if ((height != oldw || height != oldh) && params.isRoundedShape) {
            refreshRectF.set(
                paddingLeft.toFloat(),
                paddingTop.toFloat(),
                (width - paddingRight).toFloat(),
                (height - paddingBottom).toFloat()
            )
        }
    }

    private fun updateShimmerParamsOnSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        if (!params.shimmerEnabled)
            return

        val newLeft = paddingLeft + params.shadowWidth + params.strokeWidth
        val newTop = paddingTop + params.shadowWidth + params.strokeWidth
        val newRight = width - paddingRight - params.shadowWidth - params.strokeWidth
        val newBottom = height - paddingBottom - params.shadowWidth - params.strokeWidth
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

        val screenWidth = resources.displayMetrics.widthPixels
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
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (dispatchDrawFlag || !params.isRoundedShape) {
            super.dispatchDraw(canvas)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.saveLayer(refreshRectF, null)
            } else {
                canvas.saveLayer(refreshRectF, null, Canvas.ALL_SAVE_FLAG)
            }
            super.dispatchDraw(canvas)
            canvas.restore()
        }
        drawShimmer(canvas)
    }

    override fun draw(canvas: Canvas) {
        if (!params.isRoundedShape) {
            super.draw(canvas)
        } else {
            dispatchDrawFlag = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.saveLayer(refreshRectF, null)
            } else {
                canvas.saveLayer(refreshRectF, null, Canvas.ALL_SAVE_FLAG)
            }
            super.draw(canvas)
            canvas.restore()
        }
    }

    override fun onDetachedFromWindow() {
        logd("onDetachedFromWindow")
        stopAnimation()
        super.onDetachedFromWindow()
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

    //是否使用焦点动画
    private val focusAnimEnabled get() = params.scaleEnabled || params.shimmerEnabled

    private fun startAnimationOnFocused() {
        if (width == 0) {
            startAnimationPreDrawListener = ViewTreeObserver.OnPreDrawListener {
                clearPreDrawListener()
                startAnimationOnFocused()
                true
            }
            viewTreeObserver.addOnPreDrawListener(startAnimationPreDrawListener)
            return
        }
        startScaleAndShimmerAnim()
        effectView?.start()
    }

    private fun startScaleAndShimmerAnim() {
        if (!focusAnimEnabled) {
            animOnFocus?.end()
            return
        }else{
            animOnFocus?.cancel()
        }
        if (animOnFocus == null) {
            createFocusAnimator()?.let {
                animOnFocus = it
                it.start()
            }
        }else{
            animOnFocus?.start()
        }
    }

    private fun stopScaleAndShimmerAnim() {
        if (!focusAnimEnabled) {
            animOnUnfocus?.end()
            return
        }else{
            animOnUnfocus?.cancel()
        }

        if (animOnUnfocus == null) {
            createUnfocusAnimator()?.let {
                animOnUnfocus = it
                it.start()
            }
        }else{
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
            viewTreeObserver.removeOnPreDrawListener(startAnimationPreDrawListener)
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
            ObjectAnimator.ofFloat(this, "scaleX", scale)
                .setDuration(params.scaleAnimDuration)
        if (params.useBounceOnScale) {
            scaleXObjectAnimator.interpolator = BounceInterpolator()
        }
        return scaleXObjectAnimator
    }

    private fun getScaleYAnimator(scale: Float): ObjectAnimator {
        val scaleYObjectAnimator =
            ObjectAnimator.ofFloat(this, "scaleY", scale)
                .setDuration(params.scaleAnimDuration)
        if (params.useBounceOnScale) {
            scaleYObjectAnimator.interpolator = BounceInterpolator()
        }
        return scaleYObjectAnimator
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
        isSelected = gainFocus
        if (gainFocus) {
            onGainFocus()
        } else {
            stopAnimation()
        }
    }

    //获取焦点
    private fun onGainFocus() {
        if (params.bringToFrontOnFocus) {
            bringToFront()
        }
        ensureEffectViewOnGainFocus()
        startAnimationOnFocused()
    }

    private fun ensureEffectViewOnGainFocus() {
        //没有effect效果，不处理
        if (params.strokeWidth <= 0 && params.shadowWidth <= 0)
            return
        val effectView = this.effectView

        val effectViewIndex = indexOfChild(effectView)
        if (effectViewIndex < 0) {
            logd("未添加Effect View，重新添加")
            (effectView!!.parent as? ViewGroup)?.removeView(effectView)
            addView(effectView, LayoutParams(width, height))
        } else {
            if (effectView?.width != width && effectView?.height != height) {
                effectView?.updateLayoutParams<ViewGroup.LayoutParams> {
                    this.width = this@LeanbackEffectLayout.width
                    this.height = this@LeanbackEffectLayout.height
                }
                logd("Effect Size不同，修正")
            }
            if (effectViewIndex != childCount - 1) {
                logd("Effect View未在末尾，bringToFront")
                effectView?.bringToFront()
            }
        }
    }

    /**
     * Called when a new child is added to this ViewGroup. Overrides should always
     * call super.onViewAdded.
     *
     * @param child the added child view
     */
    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        child?.let {
            adjustChildMargin(it)
        }
    }

    private fun adjustChildMargin(child: View){
        if (!params.adjustChildrenMargin || child == effectView)
            return

        val adjustMargin = (params.strokeWidth + params.shadowWidth + params.childrenOffsetMargin).toInt()
        if (adjustMargin <= 0)//不需要调整
            return

        val hasAdjusted = child.getTag(params.hashCode()) as? Boolean ?: false
        if (hasAdjusted)//已调整过
            return


        val lp = child.layoutParams as? MarginLayoutParams ?: return
        child.updateLayoutParams<MarginLayoutParams> {
            lp.leftMargin += adjustMargin
            lp.topMargin += adjustMargin
            lp.rightMargin += adjustMargin
            lp.bottomMargin += adjustMargin
            //增加调整标记
            child.setTag(params.hashCode(), true)
        }
    }

    fun removeEffectView() {
        effectView?.let {
            removeView(it)
        }
    }
}