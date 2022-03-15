package bas.leanback.effect

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.IdRes
import bas.leanback.effect.R

/**
 * Created by Lucio on 2021/12/6.
 * 闪光效果
 */
class EffectParams {


    /*是否启用扫光动画*/
    var shimmerEnabled: Boolean = DEFAULT_SHIMMER_ENABLED
        private set

    /*扫光颜色*/
    var shimmerColor: Int = DEFAULT_SHIMMER_COLOR
        private set

    /*扫光动画延迟执行时间*/
    var shimmerDelay: Int = DEFAULT_SCALE_DURATION + SHIMMER_DELAY_OFFSET
        private set

    //是否使用呼吸灯效果
    var breatheEnabled: Boolean = DEFAULT_BREATHE_ENABLED
        private set

    //呼吸灯动画周期
    var breatheDuration: Int = BREATHE_DURATION
        private set

    /*边框大小，默认0，不使用边框*/
    var strokeWidth: Float = 0f
        private set(value) {
            field = value
            strokeWidthHalf = value / 2
        }

    //一半stroke大小：通常stroke的绘制是从线中间开始计算的
    internal var strokeWidthHalf: Float = 0f

    var strokeColor: Int = 0
        private set

    var shadowWidth: Float = 0f
        private set

    var shadowColor: Int = 0
        private set

    var cornerSizeTopLeft: Float = 0f
        private set
    var cornerSizeTopRight: Float = 0f
        private set
    var cornerSizeBottomLeft: Float = 0f
        private set
    var cornerSizeBottomRight: Float = 0f
        private set

    internal val cornerRadius: FloatArray

    /*是否使用缩放动画*/
    var scaleEnabled: Boolean = DEFAULT_SCALE_ENABLED
        private set

    //缩放动画时间
    var scaleAnimDuration: Int = DEFAULT_SCALE_DURATION
        private set(value) {
            field = value
            shimmerDelay = value + SHIMMER_DELAY_OFFSET
        }

    //缩放倍数
    var scaleFactor: Float = DEFAULT_SCALE_FACTOR
        private set

    //缩放是否使用弹簧效果
    var useBounceOnScale: Boolean = DEFAULT_USE_BOUNCE_ON_SCALE
        private set

    //是否是圆角形状
    val isRoundedShape: Boolean get() = cornerSizeTopLeft != 0f || cornerSizeTopRight != 0f || cornerSizeBottomRight != 0f || cornerSizeBottomLeft != 0f

    //获取焦点时是否调用bringToFront
    var bringToFrontOnFocus: Int = BRING_FLAG_NONE
        private set

    //是否校准children margin，默认true：除非您知道该含义，否则别关闭该开关
    var adjustChildrenMargin: Boolean = DEFAULT_ADJUST_CHILDREN_MARGIN
        private set

    var childrenOffsetMargin: Float = 0f
        private set

    var containsSurfaceChild: Boolean = false
        private set

    /*需要排除的id*/
    internal val excludeAdjustIds = mutableSetOf<Int>()

    internal constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.LeanbackEffectLayout)
        shimmerEnabled = ta.getBoolean(R.styleable.LeanbackEffectLayout_shimmer_bas, shimmerEnabled)
        shimmerColor = ta.getColor(R.styleable.LeanbackEffectLayout_shimmerColor_bas, shimmerColor)

        scaleEnabled = ta.getBoolean(R.styleable.LeanbackEffectLayout_scale_bas, scaleEnabled)
        scaleAnimDuration =
            ta.getInt(R.styleable.LeanbackEffectLayout_scaleDuration_bas, scaleAnimDuration)

        scaleFactor = ta.getFloat(R.styleable.LeanbackEffectLayout_scaleFactor_bas, scaleFactor)
        useBounceOnScale =
            ta.getBoolean(R.styleable.LeanbackEffectLayout_useBounceOnScale_bas, useBounceOnScale)

        breatheEnabled =
            ta.getBoolean(R.styleable.LeanbackEffectLayout_breathe_bas, breatheEnabled)
        breatheDuration =
            ta.getInteger(R.styleable.LeanbackEffectLayout_breatheDuration_bas, breatheDuration)

        strokeWidth = ta.getDimension(R.styleable.LeanbackEffectLayout_strokeWidth_bas, strokeWidth)
        strokeColor = ta.getColor(R.styleable.LeanbackEffectLayout_strokeColor_bas, strokeColor)

        shadowWidth = ta.getDimension(
            R.styleable.LeanbackEffectLayout_shadowWidth_bas,
            shadowWidth
        )
        shadowColor = ta.getColor(R.styleable.LeanbackEffectLayout_shadowColor_bas, shadowColor)

        bringToFrontOnFocus =
            ta.getInt(R.styleable.LeanbackEffectLayout_bringToFrontOnFocus_bas, bringToFrontOnFocus)

        adjustChildrenMargin = ta.getBoolean(
            R.styleable.LeanbackEffectLayout_adjustChildrenMargin_bas,
            adjustChildrenMargin
        )

        containsSurfaceChild = ta.getBoolean(R.styleable.LeanbackEffectLayout_effect_contains_surface_child_bas,containsSurfaceChild)
        val excludesIds =
            ta.getString(R.styleable.EffectConstraintLayout_excludeAdjustReferencedIDs_bas)
        parseExcludeIds(context, excludesIds)

        childrenOffsetMargin = ta.getDimension(
            R.styleable.LeanbackEffectLayout_childrenOffsetMargin_bas,
            childrenOffsetMargin
        )

        val cornerSize = ta.getDimension(R.styleable.LeanbackEffectLayout_cornerSize_bas, 0f)
        cornerSizeTopLeft =
            ta.getDimension(R.styleable.LeanbackEffectLayout_cornerSizeTopLeft_bas, cornerSize)
        cornerSizeTopRight =
            ta.getDimension(R.styleable.LeanbackEffectLayout_cornerSizeTopRight_bas, cornerSize)
        cornerSizeBottomLeft = ta.getDimension(
            R.styleable.LeanbackEffectLayout_cornerSizeBottomLeft_bas,
            cornerSize
        )
        cornerSizeBottomRight = ta.getDimension(
            R.styleable.LeanbackEffectLayout_cornerSizeBottomRight_bas,
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

    internal constructor(builder: Builder) {
        this.shimmerEnabled = builder.shimmerEnabled
        this.shimmerColor = builder.shimmerColor
        this.breatheEnabled = builder.breatheEnabled
        this.breatheDuration = builder.breatheDuration
        this.strokeWidth = builder.strokeWidth
        this.strokeColor = builder.strokeColor
        this.shadowWidth = builder.shadowWidth
        this.shadowColor = builder.shadowColor
        this.cornerSizeTopLeft = builder.cornerSizeTopLeft
        this.cornerSizeTopRight = builder.cornerSizeTopRight
        this.cornerSizeBottomLeft = builder.cornerSizeBottomLeft
        this.cornerSizeBottomRight = builder.cornerSizeBottomRight
        this.scaleEnabled = builder.scaleEnabled
        this.scaleAnimDuration = builder.scaleAnimDuration
        this.scaleFactor = builder.scaleFactor
        this.useBounceOnScale = builder.useBounceOnScale
        this.bringToFrontOnFocus = builder.bringToFrontOnFocus
        this.adjustChildrenMargin = builder.adjustChildrenMargin
        this.childrenOffsetMargin = builder.childrenOffsetMargin
        this.excludeAdjustIds.addAll(builder.excludeAdjustIds)

        cornerRadius = floatArrayOf(
            cornerSizeTopLeft, cornerSizeTopLeft,
            cornerSizeTopRight, cornerSizeTopRight,
            cornerSizeBottomRight, cornerSizeBottomRight,
            cornerSizeBottomLeft, cornerSizeBottomLeft
        )
    }

    private fun parseExcludeIds(context: Context, ids: String?) {
        excludeAdjustIds.clear()
        if (ids.isNullOrEmpty())
            return

        val tempIds = ids.split(',')
        tempIds.forEach {
            try {
                val vid = context.resources.getIdentifier(it, "id", context.packageName)
                if (vid > 0)
                    excludeAdjustIds.add(vid)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    data class Builder(
        /*是否启用扫光动画*/
        var shimmerEnabled: Boolean = DEFAULT_SHIMMER_ENABLED,

        /*扫光颜色*/
        var shimmerColor: Int = DEFAULT_SHIMMER_COLOR,

        //是否使用呼吸灯效果
        var breatheEnabled: Boolean = DEFAULT_BREATHE_ENABLED,

        //呼吸灯动画周期
        var breatheDuration: Int = BREATHE_DURATION,

        /*边框大小，默认0，不使用边框*/
        var strokeWidth: Float = 0f,

        var strokeColor: Int = 0,

        var shadowWidth: Float = 0f,
        var shadowColor: Int = 0,

        var cornerSizeTopLeft: Float = 0f,
        var cornerSizeTopRight: Float = 0f,
        var cornerSizeBottomLeft: Float = 0f,
        var cornerSizeBottomRight: Float = 0f,

        /*是否使用缩放动画*/
        var scaleEnabled: Boolean = DEFAULT_SCALE_ENABLED,

        //缩放动画时间
        var scaleAnimDuration: Int = DEFAULT_SCALE_DURATION,

        //缩放倍数
        var scaleFactor: Float = DEFAULT_SCALE_FACTOR,

        //缩放是否使用弹簧效果
        var useBounceOnScale: Boolean = DEFAULT_USE_BOUNCE_ON_SCALE,

        //获取焦点时是否调用bringToFront
        var bringToFrontOnFocus: Int = BRING_FLAG_NONE,

        //是否校准children margin，默认true：除非您知道该含义，否则别关闭该开关
        var adjustChildrenMargin: Boolean = DEFAULT_ADJUST_CHILDREN_MARGIN,

        var childrenOffsetMargin: Float = 0f
    ) {

        /*需要排除的id*/
        @JvmField
        internal var excludeAdjustIds: MutableSet<Int> = mutableSetOf<Int>()

        /**
         * 设置排除调整marign的ViewId
         */
        fun setExcludeAdjustMarginViewIds(vararg @IdRes id: Int) {
            excludeAdjustIds.clear()
            excludeAdjustIds.addAll(id.toList())
        }

        fun build(): EffectParams {
            return EffectParams(this)
        }

        /**
         * Returns a string representation of the object.
         */
        override fun toString(): String {
            return super.toString() + "\nexcludeAdjustIds=@${excludeAdjustIds.hashCode()}"
        }
    }

    companion object {

        const val DEFAULT_SCALE_ENABLED = true
        const val DEFAULT_SCALE_DURATION = 300
        const val DEFAULT_SCALE_FACTOR = 1.1f
        const val DEFAULT_USE_BOUNCE_ON_SCALE = false

        const val DEFAULT_SHIMMER_ENABLED = true

        const val DEFAULT_SHIMMER_COLOR = 0x66FFFFFF

        const val DEFAULT_BREATHE_ENABLED = true

        const val BREATHE_DURATION = 4000


        const val DEFAULT_ADJUST_CHILDREN_MARGIN = true

        const val SHIMMER_DELAY_OFFSET = 300

        const val BRING_FLAG_NONE = 0
        const val BRING_FLAG_SELF = 1
        const val BRING_FLAG_SELF_PARENT = 2
        const val BRING_FLAG_PARENT = 3
    }

}