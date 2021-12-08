package bas.leanback.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import bas.leanback.core.R
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
    private var shimmerTranslate = 0f

    private var refreshRectF: RectF = RectF()
    private var dispatchDrawFlag = false

    private var isShimmerTranslating: Boolean = false
    private var effectView: LeanbackEffectView? = null

    private val frameRectF: RectF = RectF()
    private var startAnimationPreDrawListener: ViewTreeObserver.OnPreDrawListener? = null
    private var mAnimatorSet: AnimatorSet? = null

    override fun isInEditMode(): Boolean {
        return true
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(width, height, oldw, oldh)
        if (effectView == null) {
            //等布局大小确定之后，添加同等大小的效果View：实现阴影、边框、呼吸灯
            effectView = LeanbackEffectView(params, context)
            addView(effectView, LayoutParams(width, height))
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
        frameRectF.set(newLeft,newTop,newRight,newBottom)

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
        shimmerLinearGradient!!.setLocalMatrix(shimmerGradientMatrix)
        canvas.drawPath(shimmerPath, shimmerPaint)
        canvas.restore()
    }

    private fun setShimmerAnimating(shimmerAnimating: Boolean) {
        isShimmerTranslating = shimmerAnimating
//        if (params.shimmerEnabled && shimmerAnimating) {
//            shimmerLinearGradient = LinearGradient(
//                0f,
//                0f,
//                frameRectF.width(),
//                frameRectF.height(),
//                intArrayOf(
//                    0x00FFFFFF,
//                    reduceColorAlphaValueToZero(params.shimmerColor),
//                    params.shimmerColor,
//                    reduceColorAlphaValueToZero(params.shimmerColor),
//                    0x00FFFFFF
//                ),
//                floatArrayOf(0f, 0.2f, 0.5f, 0.8f, 1f),
//                Shader.TileMode.CLAMP
//            )
//            shimmerPaint.shader = shimmerLinearGradient
//        }
    }

    private fun reduceColorAlphaValueToZero(actualColor: Int): Int {
        return Color.argb(
            0x1A,
            Color.red(actualColor),
            Color.green(actualColor),
            Color.blue(actualColor)
        )
    }

    fun startAnimation() {
        if (width == 0) {
            startAnimationPreDrawListener = object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    viewTreeObserver.removeOnPreDrawListener(this)
                    startAnimation()
                    return true
                }
            }
            viewTreeObserver.addOnPreDrawListener(startAnimationPreDrawListener)
            return
        }
        if (null != mAnimatorSet) {
            mAnimatorSet!!.cancel()
        }
        effectView?.start()
        createAnimatorSet(true)
        mAnimatorSet!!.start()
    }

    fun stopAnimation() {
        if (startAnimationPreDrawListener != null) {
            viewTreeObserver.removeOnPreDrawListener(startAnimationPreDrawListener)
        }
        if (null != mAnimatorSet) {
            mAnimatorSet!!.cancel()
        }
        createAnimatorSet(false)
        effectView?.stop()
        mAnimatorSet!!.start()
    }

    private fun createAnimatorSet(isStart: Boolean) {
        val together: MutableList<Animator> = ArrayList()
        if (isStart) {
            together.add(getScaleXAnimator(params.scaleFactor))
            together.add(getScaleYAnimator(params.scaleFactor))
        } else {
            together.add(getScaleXAnimator(1.0f))
            together.add(getScaleYAnimator(1.0f))
        }
        val sequentially: MutableList<Animator> = ArrayList()
        if (params.shimmerEnabled && isStart) {
            sequentially.add(shimmerAnimator)
        }
        mAnimatorSet = AnimatorSet()
        mAnimatorSet!!.playTogether(together)
        mAnimatorSet!!.playSequentially(sequentially)
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

    private val shimmerAnimator: ObjectAnimator
        private get() {
            val mShimmerAnimator = ObjectAnimator.ofFloat(this, "shimmerTranslate", -1f, 1f)
            mShimmerAnimator.interpolator = DecelerateInterpolator(1f)
            val screenWidth = resources.displayMetrics.widthPixels
            val max = if (width >= height) width else height
            val duration = if (max > screenWidth / 3) screenWidth / 3 else max
            mShimmerAnimator.duration = (duration * 3).toLong()
            mShimmerAnimator.startDelay = params.shimmerDelay
            mShimmerAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    setShimmerAnimating(true)
                }

                override fun onAnimationEnd(animation: Animator) {
                    setShimmerAnimating(false)
                }
            })
            return mShimmerAnimator
        }

    fun setShimmerTranslate(translate: Float) {
        if (params.shimmerEnabled && this.shimmerTranslate != translate) {
            this.shimmerTranslate = translate
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
        isSelected = gainFocus
        if (gainFocus) {
            if (params.bringToFrontOnFocus) {
                bringToFront()
            }
            startAnimation()
        } else {
            stopAnimation()
        }
    }

    init {
        setWillNotDraw(false)
        params = EffectParams(context, attrs)
    }

}