//package bas.leanback.v2
//
//import android.graphics.*
//import android.view.View
//import bas.leanback.effect.EffectParams
//
///**
// * 四周边框效果
// */
//class StrokeEffect(val view:View,params: EffectParams) : AbstractEffect(params) {
//
//    private val strokePaint = Paint()
//    private val strokeRectF = RectF()
//    private val strokePath: Path = Path()
//
//    private val strokeEnabled: Boolean get() = params.strokeWidth > 0
//
//    init {
//        if (strokeEnabled) {
//            this.strokePaint.color = params.strokeColor
//            this.strokePaint.strokeWidth = params.strokeWidth
//            this.strokePaint.style = Paint.Style.STROKE
////            this.strokePaint.maskFilter = BlurMaskFilter(0.5f, BlurMaskFilter.Blur.NORMAL)
//        }
//    }
//
//    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
//        if (!strokeEnabled)
//            return
//
//        //todo 很奇怪，不管怎么计算，绘制的路径都有些许偏差
//        view.getDrawingRect()
//        val newLeft = view.paddingLeft + params.shadowWidth
//        val newTop = view.paddingTop + params.shadowWidth
//        val newRight =width - view.paddingRight - params.shadowWidth
//        val newBottom = height - view.paddingBottom - params.shadowWidth
//
//        if (newLeft == strokeRectF.left && newTop == strokeRectF.top && newRight == strokeRectF.right && newBottom == strokeRectF.bottom)
//            return
//
//        strokePath.reset()
//        strokeRectF.set(newLeft, newTop, newRight, newBottom)
//        //必须使用这种方式，否则在某些情况下会出现中间有个阴影色的色块
//        if (params.isRoundedShape) {
//            strokePath.addRoundRect(strokeRectF, params.cornerRadius, Path.Direction.CW)
//        } else {
//            strokePath.addRect(strokeRectF, Path.Direction.CW)
//        }
//    }
//
//    override fun onDraw(canvas: Canvas) {
//        if (strokeEnabled) {
//            canvas.save()
//            canvas.drawPath(this.strokePath, this.strokePaint)
//            canvas.restore()
//        }
//    }
//
//
//}