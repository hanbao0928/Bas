package bas.droid.core.graphics

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.shapes.RectShape
import kotlin.math.min
import kotlin.properties.Delegates


/**
 * Created by Lucio on 2019/9/26.
 * 圆弧四边形：即四边形的左右两边为圆形
 * @see [android.graphics.drawable.shapes.*] 官方的各种shape
 * @see [androidx.swiperefreshlayout.widget.CircleImageView.OvalShadow] 阴影的绘制方法
 */
 class CircleArcRectShape : RectShape() {

    private var pathToDraw: Path = Path()

    private var outerRadii1: FloatArray by Delegates.notNull<FloatArray>()

    private var currentHeight: Float = -1f

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawPath(pathToDraw, paint)
    }

    //修改onResize方法
    override fun onResize(w: Float, h: Float) {
        val height = min(w, h)//表示在高大于宽时，默认显示为圆形
        super.onResize(w, height)
        if (currentHeight == height)
            return

        val outRadius = height / 2 + 1
        /*参数分别对应：从左上->右上->右下->左下方向，一个角对应两个参数，比如第一个参数为左上角左边圆弧半径，第二个参数为左上角上边圆弧半径*/
        outerRadii1 = floatArrayOf(
            outRadius,
            outRadius,
            outRadius,
            outRadius,
            outRadius,
            outRadius,
            outRadius,
            outRadius
        )//左上x2,右上x2,右下x2,左下x2，注意顺序（顺时针依次设置）

        val r = rect()
        pathToDraw.reset()
        pathToDraw.addRoundRect(r, outerRadii1, Path.Direction.CW)
//        if (outerRadii1 != null) {
//            pathToDraw.addRoundRect(r, outerRadii1, Path.Direction.CW)
//        } else {
//            pathToDraw.addRect(r, Path.Direction.CW)
//        }
        currentHeight = height
    }


}