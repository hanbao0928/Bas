package bas.leanback.ui

import android.animation.*
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * user by pj567
 * date on 2019/12/16.
 */
internal class LeanbackEffectView @JvmOverloads constructor(
    val params: EffectParams,
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val strokePaint = Paint()
    private val shadowPaint = Paint()
    private val shadowRectF = RectF()
    private val strokePath: Path = Path()


    //呼吸动画
    private val breatheAnim: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(this, "alpha", 1f, 0.2f, 1f).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = params.breatheDuration
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            startDelay = params.shimmerDelay
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationCancel(animation: Animator) {
                    super.onAnimationCancel(animation)
                    alpha = 1f
                }
            })
        }
    }

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
        //必须使用这种方式，否则在某些情况下会出现中间有个阴影色的色块
        if (params.cornerSizeTopLeft != 0f || params.cornerSizeTopRight != 0f || params.cornerSizeBottomLeft != 0f || params.cornerSizeBottomRight != 0f) {
            strokePath.addRoundRect(shadowRectF, params.cornerRadius, Path.Direction.CW)
        } else {
            strokePath.addRoundRect(shadowRectF, 0f, 0f, Path.Direction.CW)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (params.shadowWidth > 0) {
            canvas.save()
            canvas.drawPath(this.strokePath, this.shadowPaint)
            canvas.restore()
        }
        if (params.strokeWidth > 0) {
            canvas.save()
            canvas.drawPath(this.strokePath, this.strokePaint)
            canvas.restore()
        }
    }

    fun start() {
        visibility = VISIBLE
        if (params.breatheEnabled) {
            breatheAnim.cancel()
            breatheAnim.start()
        } else {
            alpha = 1f
        }
    }

    fun stop() {
        visibility = GONE
        if (params.breatheEnabled) {
            breatheAnim.cancel()
        }
    }

}