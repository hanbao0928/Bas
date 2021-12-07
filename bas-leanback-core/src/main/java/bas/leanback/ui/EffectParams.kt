package bas.leanback.ui

import android.content.Context
import android.util.AttributeSet
import bas.leanback.core.R

/**
 * Created by Lucio on 2021/12/6.
 * 闪光效果
 */
class EffectParams {
    //闪光动画延迟执行时间
    val shimmerAnimEnabled: Boolean
    val shimmerDelay: Long
    val shimmerColor: Int

    //是否使用呼吸灯效果
    val breatheAnimEnabled:Boolean
    //呼吸灯动画周期
    val breatheDuration:Long

    val strokeWidth: Float
    val strokeColor: Int
    val shadowWidth: Float
    val shadowColor: Int

    val cornerSizeTopLeft: Float
    val cornerSizeTopRight: Float
    val cornerSizeBottomLeft: Float
    val cornerSizeBottomRight: Float
    val cornerRadius: FloatArray

    //缩放动画时间
    val scaleAnimDuration: Long


    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.LeanbackView)

        shimmerAnimEnabled =
            ta.getBoolean(R.styleable.ShimmerShadowLayout_shimmerAnim_bas, DEFAULT_SHIMMER_ENABLED)
        shimmerColor =
            ta.getColor(R.styleable.ShimmerShadowLayout_shimmerColor_bas, DEFAULT_SHIMMER_COLOR)


        scaleAnimDuration =
            ta.getInt(R.styleable.ShimmerShadowLayout_scaleDuration_bas, DEFAULT_SCALE_DURATION)
                .toLong()
        shimmerDelay = scaleAnimDuration + 100

//        useBorderEffect = ta.getBoolean(R.styleable.LeanbackView_border_effect_bas, false)
//        useShimmerEffect = ta.getBoolean(R.styleable.LeanbackView_shimmer_effect_bas, false)
//
//        useBreathingAnim = ta.getBoolean(R.styleable.LeanbackView_breathing_anim_bas, true)

        breatheAnimEnabled =
            ta.getBoolean(R.styleable.ShimmerShadowLayout_breatheAnim_bas, DEFAULT_BREATHE_ENABLED)
        breatheDuration =
            ta.getInteger(R.styleable.ShimmerShadowLayout_breatheDuration_bas, BREATHE_DURATION).toLong()


        strokeWidth = ta.getDimension(
            R.styleable.LeanbackView_strokeWidth_bas,
            0f
        )
        strokeColor = ta.getColor(R.styleable.LeanbackView_strokeColor_bas, 0)

        shadowWidth = ta.getDimension(
            R.styleable.LeanbackView_shadowWidth_bas,
            0f
        )
        shadowColor = ta.getColor(R.styleable.LeanbackView_shadowColor_bas, 0)

        val cornerSize = ta.getDimension(R.styleable.LeanbackView_cornerSize_bas, 0f)
        cornerSizeTopLeft =
            ta.getDimension(R.styleable.LeanbackView_cornerSizeTopLeft_bas, cornerSize)
        cornerSizeTopRight =
            ta.getDimension(R.styleable.LeanbackView_cornerSizeTopRight_bas, cornerSize)
        cornerSizeBottomLeft = ta.getDimension(
            R.styleable.LeanbackView_cornerSizeBottomLeft_bas,
            cornerSize
        )
        cornerSizeBottomRight = ta.getDimension(
            R.styleable.LeanbackView_cornerSizeBottomRight_bas,
            cornerSize
        )

        cornerRadius = floatArrayOf(
            cornerSizeTopLeft, cornerSizeTopLeft,
            cornerSizeTopRight, cornerSizeTopRight,
            cornerSizeBottomRight, cornerSizeBottomRight,
            cornerSizeBottomLeft, cornerSizeBottomLeft
        )

        ta.recycle()
    }

    companion object {

        const val DEFAULT_SCALE_DURATION = 300

        const val DEFAULT_SHIMMER_ENABLED = true

        const val DEFAULT_SHIMMER_COLOR = 0x66FFFFFF

        const val DEFAULT_BREATHE_ENABLED = true

        const val BREATHE_DURATION = 4000
    }

}