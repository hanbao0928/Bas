package bas.leanback.v2

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt

class LeanbackEffectLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint()
    private val paint2 = Paint()
    private val rect2 = RectF()
    private val rect = Rect()

    init {
        paint.setShadowLayer(30f, 0f, 0f, Color.RED)
        paint.strokeWidth = 20f
        paint.style = Paint.Style.STROKE
        paint.setColor(Color.BLUE)
        this.setLayerType(LAYER_TYPE_SOFTWARE, null)

        paint2.color = Color.RED
        paint2.style = Paint.Style.FILL
        paint2.strokeWidth =  15f

    }
//
//    /**
//     * 关于绘制顺序，参考：https://rengwuxian.com/ui-1-5/
//     */
//    override fun dispatchDraw(canvas: Canvas) {
//
//        super.dispatchDraw(canvas)
//    }
//
//    /**
//     * 关于绘制顺序，参考：https://rengwuxian.com/ui-1-5/
//     */
//    override fun onDraw(canvas: Canvas) {
//
//        drawBorder(canvas)
//        super.onDraw(canvas)
//
//    }

    override fun draw(canvas: Canvas) {
        drawBorder(canvas)
        super.draw(canvas)
        drawBorder(canvas)
    }

    fun setStroke(size: Float) {
        paint.strokeWidth = size
        invalidate()
    }

    fun setShadowLayer(radius: Float, dx: Float, dy: Float, @ColorInt shadowColor: Int) {
        paint.setShadowLayer(radius, dx, dy, shadowColor)
        invalidate()
    }

    fun setDisableStroke() {
        paint.strokeWidth = 0f
        invalidate()
    }

    fun setDisableShadow() {
        paint.clearShadowLayer()
        invalidate()
    }


    private fun drawBorder(canvas: Canvas) {
        this.getDrawingRect(rect)
        canvas.drawRect(rect, paint)
//        canvas.saveLayer(rect2, null, Canvas.ALL_SAVE_FLAG)

        canvas.translate(-30f,0f)
        canvas.drawLine(30f,height/2f,width.toFloat()+30f,height/2f,paint2)

        canvas.drawLine(-10f,width/2f,width+10f,width/2f,paint2)

        canvas.drawCircle(this.width + 20f, this.height + 20f, 20f, paint2)
        canvas.drawCircle(-20f, -20f, 20f, paint2)
        canvas.drawCircle(width / 2f, height / 2f, 20f, paint2)
//        canvas.restore()

    }
}