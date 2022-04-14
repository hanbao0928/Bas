package bas.droid.core.graphics

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable


/**
 * 半圆弧四边形：左右两边为半圆弧
 * @param color 颜色值
 * @param alpha 透明度
 */
fun CircleArcShapeDrawable(
    color: Int,
    @androidx.annotation.IntRange(from = 0, to = 255) alpha: Int = 255
): ShapeDrawable {
    val rectShape = ShapeDrawable(CircleArcRectShape())
    rectShape.paint.apply {
        this.color = color
        isAntiAlias = true
        style = Paint.Style.FILL
        this.alpha = alpha
    }
    return rectShape
}

/**
 * 带状态的半圆弧四边形
 */
fun CircleArcStateListDrawable(defaultColor: Int, activeColor: Int): Drawable {
    val defaultDrawable = CircleArcShapeDrawable(defaultColor)
    val activeDrawable = CircleArcShapeDrawable(activeColor)
    val drawable = StateListDrawable()
    drawable.addState(intArrayOf(android.R.attr.state_pressed), activeDrawable)
    drawable.addState(intArrayOf(), defaultDrawable)
    return drawable
}