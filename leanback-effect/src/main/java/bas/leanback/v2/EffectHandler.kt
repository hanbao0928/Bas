package bas.leanback.v2

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import bas.leanback.effect.EffectParams

internal class EffectHandler @JvmOverloads constructor(
    val params: EffectParams,
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //呼吸动画
    private val breatheAnim: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(this, "alpha", 1f, 0.2f, 1f).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = params.breatheDuration.toLong()
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            startDelay = params.shimmerDelay.toLong()
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

        visibility = GONE
    }



    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(width, height, oldw, oldh)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    fun startAnimation() {
        visibility = VISIBLE
        if (params.breatheEnabled) {
            if (breatheAnim.isStarted)
                breatheAnim.cancel()
            breatheAnim.start()
        } else {
            alpha = 1f
        }
    }

    fun stopAnimation() {
        visibility = GONE
        if (params.breatheEnabled) {
            breatheAnim.cancel()
        } else {
            breatheAnim.end()
        }
    }

}