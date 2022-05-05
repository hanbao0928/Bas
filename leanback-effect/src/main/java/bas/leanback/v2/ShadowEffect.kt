package bas.leanback.v2

import android.graphics.*
import bas.leanback.effect.EffectParams

/**
 * 四周阴影效果
 */
class ShadowEffect(params: EffectParams) : AbstractEffect(params) {

    private val shadowPaint = Paint()
    private val shadowRectF = RectF()
    private val shadowPath: Path = Path()

    private val shadowEnabled: Boolean get() = params.shadowWidth > 0

    init {
        if (shadowEnabled) {
            this.shadowPaint.maskFilter =
                BlurMaskFilter(params.shadowWidth, BlurMaskFilter.Blur.OUTER)
            this.shadowPaint.strokeWidth = 1f
            this.shadowPaint.color = params.shadowColor
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (shadowEnabled) {
            canvas.save()
            canvas.drawPath(this.shadowPath, this.shadowPaint)
            canvas.restore()
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        if (!shadowEnabled)
            return

        val newLeft = params.shadowWidth
        val newTop = params.shadowWidth
        val newRight = width - params.shadowWidth
        val newBottom = height - params.shadowWidth

        if (newLeft == shadowRectF.left && newTop == shadowRectF.top && newRight == shadowRectF.right && newBottom == shadowRectF.bottom)
            return

        shadowPath.reset()
        shadowRectF.set(newLeft, newTop, newRight, newBottom)
        //必须使用这种方式，否则在某些情况下会出现中间有个阴影色的色块
        if (params.isRoundedShape) {
            shadowPath.addRoundRect(shadowRectF, params.cornerRadius, Path.Direction.CW)
        } else {
            shadowPath.addRoundRect(shadowRectF, 0f, 0f, Path.Direction.CW)
        }
    }

}