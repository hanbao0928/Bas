package bas.leanback.ui

import android.animation.*
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * user by pj567
 * date on 2019/12/16.
 */
internal class BreatheShadowView @JvmOverloads constructor(
    val params: EffectParams,
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val strokePaint = Paint()
    private val shadowPaint = Paint()
    private val shadowRectF = RectF()
    private val strokePath: Path = Path()

    private var mAnimatorSet: AnimatorSet? = null
    private var isBreathe = true
    private var isShadow = true
    private var isBorder = true

    init {
        // 需禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        this.shadowPaint.maskFilter = BlurMaskFilter(params.shadowWidth, BlurMaskFilter.Blur.OUTER)
        this.shadowPaint.strokeWidth = 1f
        this.shadowPaint.color = params.shadowColor

        this.strokePaint.color = params.strokeColor
        this.strokePaint.strokeWidth = params.strokeWidth
        this.strokePaint.style = Paint.Style.STROKE
        this.strokePaint.maskFilter = BlurMaskFilter(0.5f, BlurMaskFilter.Blur.NORMAL)
        visibility = GONE
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(width, height, oldw, oldh)
        strokePath.reset()
        shadowRectF.set(
            params.shadowWidth,
            params.shadowWidth,
            width - params.shadowWidth,
            height - params.shadowWidth
        )
        if (params.cornerSizeTopLeft != 0f || params.cornerSizeTopRight != 0f || params.cornerSizeBottomLeft != 0f || params.cornerSizeBottomRight != 0f) {
            strokePath.addRoundRect(shadowRectF, params.cornerRadius, Path.Direction.CW)
        } else {
            strokePath.addRoundRect(shadowRectF, 0f,0f, Path.Direction.CW)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if(shadowRectF.width()  == 0f || shadowRectF.height() == 0f)
            return
        if (isShadow) {
            canvas.save()
            canvas.drawPath(this.strokePath, this.shadowPaint)
            canvas.restore()
        }
        if (isBorder) {
            canvas.save()
            canvas.drawPath(this.strokePath, this.strokePaint)
            canvas.restore()
        }
    }

    private fun createAnimatorSet() {
        mAnimatorSet = AnimatorSet()
        mAnimatorSet!!.playSequentially(alphaAnimator)
    }

    private val alphaAnimator: ObjectAnimator
        private get() {
            val alphaAnimator: ObjectAnimator
            alphaAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0.2f, 1f)
            alphaAnimator.interpolator = AccelerateDecelerateInterpolator()
            alphaAnimator.duration = params.breatheDuration
            alphaAnimator.repeatMode = ValueAnimator.REVERSE
            alphaAnimator.repeatCount = ValueAnimator.INFINITE
            alphaAnimator.startDelay = params.shimmerDelay
            alphaAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationCancel(animation: Animator) {
                    super.onAnimationCancel(animation)
                    alpha = 1f
                }
            })
            return alphaAnimator
        }

    fun start() {
        visibility = VISIBLE
        if (isBreathe) {
            if (null != mAnimatorSet) {
                mAnimatorSet!!.cancel()
            }
            createAnimatorSet()
            mAnimatorSet!!.start()
        } else {
            alpha = 1f
        }
    }

    fun stop() {
        visibility = GONE
        if (isBreathe) {
            if (null != mAnimatorSet) {
                mAnimatorSet!!.cancel()
            }
        }
    }

    fun setStartDelay(mStartDelay: Long) {
//        this.mStartDelay = mStartDelay
    }

    fun setShadowWidth(mShadowWidth: Float) {
//        if (this.mShadowWidth != mShadowWidth) {
//            this.mShadowWidth = mShadowWidth
//            if (this.shadowPaint != null) {
//                this.shadowPaint!!.maskFilter = BlurMaskFilter(
//                    if (mShadowWidth == 0f) 0.1f else mShadowWidth,
//                    BlurMaskFilter.Blur.OUTER
//                )
//            }
//        }
    }

    fun setBorderWidth(mBorderWidth: Float) {
//        this.mBorderWidth = mBorderWidth
//        this.strokePaint.strokeWidth = mBorderWidth
    }

    fun setTopLeftRadius(mTopLeftRadius: Float) {
//        this.mTopLeftRadius = mTopLeftRadius
    }

    fun setTopRightRadius(mTopRightRadius: Float) {
//        this.mTopRightRadius = mTopRightRadius
    }

    fun setBottomLeftRadius(mBottomLeftRadius: Float) {
//        this.mBottomLeftRadius = mBottomLeftRadius
    }

    fun setBottomRightRadius(mBottomRightRadius: Float) {
//        this.mBottomRightRadius = mBottomRightRadius
    }

    fun setShadow(shadow: Boolean) {
        isShadow = shadow
    }

    fun setBorder(border: Boolean) {
        isBorder = border
    }

    fun setShadowColor(mShadowColor: Int) {
//        this.mShadowColor = mShadowColor
//        this.shadowPaint.color = mShadowColor
    }

    fun setBorderColor(mBorderColor: Int) {
//        this.mBorderColor = mBorderColor
//        this.strokePaint.color = mBorderColor
    }

    fun setBreatheDuration(mBreatheDuration: Long) {
//        this.mBreatheDuration = mBreatheDuration
    }

    fun setBreathe(breathe: Boolean) {
        isBreathe = breathe
    }

    private fun pt2px(value: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PT,
            value,
            resources.displayMetrics
        ) + 0.5f
    }

}