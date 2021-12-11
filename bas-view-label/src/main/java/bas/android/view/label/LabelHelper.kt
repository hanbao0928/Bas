//package bas.android.view.label
//
//import android.content.res.TypedArray
//import android.graphics.*
//import android.text.TextPaint
//import android.util.AttributeSet
//import android.view.Gravity
//import android.view.View
//import androidx.core.view.isVisible
//
///**
// * Created by Lucio on 2021/12/10.
// */
//class LabelHelper(val view: View, attrs: AttributeSet? = null, defStyleAttr: Int = 0) {
//
//    companion object {
//
//        @JvmOverloads
//        @JvmStatic
//        fun create(view: View, attrs: AttributeSet? = null, defStyleAttr: Int = 0): LabelHelper {
//            return create(view, attrs, defStyleAttr)
//        }
//
//
//        private const val DEFAULT_TEXT_SIZE = 14
//        private const val DEFAULT_TEXT_COLOR = 0x000000
//
//
//
//        /*默认偏移角度45度*/
//        private const val DEFAULT_DEGREES = 45
//
//
//    }
//
//    private val textPaint = TextPaint().apply {
//        isDither = true
//        isAntiAlias = true
//        strokeJoin = Paint.Join.ROUND
//        strokeCap = Paint.Cap.SQUARE
//    }
//
//
//    private val frameRectF = RectF()
//
//    private val textRectF = RectF()
//
//    private val backgroundRectF = RectF()
//
//    private val backgroundPaint: Paint = Paint()
//
//    private val distance: Int
//
//    private val padding: Int
////
////    /*绘制路径*/
////    private val rectPath: Path
//
//    /*绘制位置*/
//    private val location: Int
//
//    /*背景是否铺满*/
//    private val backgroundPaved: Boolean
//
//    /*旋转度数*/
//    private val degree:Int
//
////    private val height: Int
////    private val strokeWidth
////
////    private val backgroundColor
////    private val strokeColor
////
////    private val visual
//
//
//    private val density = view.resources.displayMetrics.density
//
//    private fun dp(value: Float): Int {
//        return (value * density).toInt()
//    }
//
//    private fun sp(value: Int): Float {
//        return value * view.resources.displayMetrics.scaledDensity
//    }
//
//    init {
//        val attributes: TypedArray = view.context.obtainStyledAttributes(
//            attrs,
//            R.styleable.LabelView,
//            defStyleAttr,
//            defStyleAttr
//        )
//
//
//        textPaint.textSize = textSize.toFloat()
//        textPaint.color = textColor
//        textPaint.typeface = Typeface.defaultFromStyle(textStyle)
//
//        padding = attributes.getDimensionPixelSize(
//            R.styleable.LabelView_label_distance,
//            dp(DEFAULT_PADDING)
//        )
//
//        backgroundPaved = attributes.getBoolean(R.styleable.LabelView_label_backgroundPaved, false)
//
//        degree = attributes.getInteger(R.styleable.LabelView_label_degree, DEFAULT_DEGREES)
//
////        height = attributes.getDimensionPixelSize(
////            R.styleable.LabelView_label_height,
////            dip2Px(com.lid.lib.LabelViewHelper.DEFAULT_HEIGHT.toFloat())
////        )
////        strokeWidth = attributes.getDimensionPixelSize(
////            R.styleable.LabelView_label_strokeWidth,
////            dip2Px(com.lid.lib.LabelViewHelper.DEFAULT_STROKE_WIDTH.toFloat())
////        )
////
////        backgroundColor = attributes.getColor(
////            R.styleable.LabelView_label_backgroundColor,
////            com.lid.lib.LabelViewHelper.DEFAULT_BACKGROUND_COLOR
////        )
////        strokeColor = attributes.getColor(
////            R.styleable.LabelView_label_strokeColor,
////            com.lid.lib.LabelViewHelper.DEFAULT_STROKE_COLOR
////        )
////
////        visual = attributes.getBoolean(R.styleable.LabelView_label_visual, true)
//
//        attributes.recycle()
//
//
//
////
////        rectPaint = Paint()
////        rectPaint.setDither(true)
////        rectPaint.setAntiAlias(true)
////        rectPaint.setStyle(Paint.Style.FILL)
////
////        rectStrokePaint = Paint()
////        rectStrokePaint.setDither(true)
////        rectStrokePaint.setAntiAlias(true)
////        rectStrokePaint.setStyle(Paint.Style.STROKE)
////
////        rectPath = Path()
////
////        textPath = Path()
////
////
////
////        textBound = Rect()
//    }
//
//    public fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        if (w == oldw && h == oldh)
//            return
////        if(backgroundPaved){
////            updateRectPathPaved(w, h)
////        }else{
////            updateRectPathDistanced(w, h)
////        }
//
//        updateRectPathDistanced(w, h)
//    }
//
//    private fun updateRectPathDistanced(w: Int, h: Int){
//        frameRectF.set(0f,0f, height.toFloat(), (height * 2).toFloat())
//        backgroundRectF.set(frameRectF)
//        textRectF.set(frameRectF)
//        textRectF.top = textRectF.top + distance
//        if(!backgroundPaved){
//            backgroundRectF.top = backgroundRectF.top + distance
//        }
//        val rotateXAnchor = Math.tan(degree.toDouble()) * frameRectF.height()
//
////        rectPath.reset()
////        val textHeight: Float = textPaint.descent() - textPaint.ascent()
////        val textRectHeight = textHeight + padding * 2
////        val rectHeight = textRectHeight + distance
////
////
////        val delta: Double = (textHeight + padding * 2) * Math.sqrt(2.0)
//        when(location){
//            RIGHT_TOP ->{
////                rectPath.moveTo(0f, 0f)
////                rectPath.lineTo(delta.toFloat(), 0f)
////                rectPath.lineTo(size.toFloat(), (size - delta) as Float)
////                rectPath.lineTo(size.toFloat(), size.toFloat())
////                rectPath.close()
////                canvas.drawPath(mPath, backgroundPaint)
////                drawText(
////                    size,
////                    com.flyco.labelview.LabelView.DEFAULT_DEGREES.toFloat(),
////                    canvas,
////                    textHeight,
////                    true
////                )
//            }
//            RIGHT_BOTTOM ->{
////                mPath.moveTo(0f, size.toFloat())
////                mPath.lineTo(delta.toFloat(), size.toFloat())
////                mPath.lineTo(size.toFloat(), delta.toFloat())
////                mPath.lineTo(size.toFloat(), 0f)
////                mPath.close()
////                canvas.drawPath(mPath, mBackgroundPaint)
////                drawText(
////                    size,
////                    -com.flyco.labelview.LabelView.DEFAULT_DEGREES.toFloat(),
////                    canvas,
////                    textHeight,
////                    false
////                )
//            }
//            LEFT_BOTTOM ->{
////                mPath.moveTo(0f, 0f)
////                mPath.lineTo(0f, delta.toFloat())
////                mPath.lineTo((size - delta) as Float, size.toFloat())
////                mPath.lineTo(size.toFloat(), size.toFloat())
////                mPath.close()
////                canvas.drawPath(mPath, mBackgroundPaint)
////                drawText(
////                    size,
////                    com.flyco.labelview.LabelView.DEFAULT_DEGREES.toFloat(),
////                    canvas,
////                    textHeight,
////                    false
////                )
//            }
//            else->{
////                rectPath.moveTo(0f, (size - delta) as Float)
////                mPath.lineTo(0f, size.toFloat())
////                mPath.lineTo(size.toFloat(), 0f)
////                mPath.lineTo((size - delta) as Float, 0f)
////                mPath.close()
////                canvas.drawPath(mPath, mBackgroundPaint)
////                drawText(
////                    size,
////                    -com.flyco.labelview.LabelView.DEFAULT_DEGREES.toFloat(),
////                    canvas,
////                    textHeight,
////                    true
////                )
//            }
//        }
//    }
////    private fun updateRectPathPaved(w: Int, h: Int) {
////        rectPath.reset()
////        when (location) {
////            RIGHT_TOP -> {
////                mPath.reset()
////                mPath.moveTo(size.toFloat(), 0f)
////                mPath.lineTo(0f, 0f)
////                mPath.lineTo(size.toFloat(), size.toFloat())
////                mPath.close()
////                canvas.drawPath(mPath, mBackgroundPaint)
////                drawTextWhenFill(
////                    size,
////                    com.flyco.labelview.LabelView.DEFAULT_DEGREES.toFloat(),
////                    canvas,
////                    true
////                )
////            }
////            LEFT_BOTTOM -> {
////                mPath.moveTo(0f, size.toFloat())
////                mPath.lineTo(0f, 0f)
////                mPath.lineTo(size.toFloat(), size.toFloat())
////                mPath.close()
////                canvas.drawPath(mPath, mBackgroundPaint)
////                drawTextWhenFill(
////                    size,
////                    com.flyco.labelview.LabelView.DEFAULT_DEGREES.toFloat(),
////                    canvas,
////                    false
////                )
////            }
////            RIGHT_BOTTOM -> {
////                mPath.moveTo(size.toFloat(), size.toFloat())
////                mPath.lineTo(0f, size.toFloat())
////                mPath.lineTo(size.toFloat(), 0f)
////                mPath.close()
////                canvas.drawPath(mPath, mBackgroundPaint)
////                drawTextWhenFill(
////                    size,
////                    -com.flyco.labelview.LabelView.DEFAULT_DEGREES.toFloat(),
////                    canvas,
////                    false
////                )
////            }
////            else -> {
////                rectPath.moveTo(0f, 0f)
////                rectPath.lineTo(0f, size.toFloat())
////                rectPath.lineTo(size.toFloat(), 0f)
////                rectPath.close()
////                canvas.drawPath(mPath, backgroundPaint)
////                drawTextWhenFill(
////                    size,
////                    -com.flyco.labelview.LabelView.DEFAULT_DEGREES.toFloat(),
////                    canvas,
////                    true
////                )
////            }
////        }
////    }
//
//
//    protected fun onDraw(canvas: Canvas) {
//        if (text.isNullOrEmpty() || !view.isVisible)
//            return
//
//    }
//
////
////    private fun drawText(
////        size: Int,
////        degrees: Float,
////        canvas: Canvas,
////        textHeight: Float,
////        isTop: Boolean
////    ) {
////        canvas.save()
////        canvas.rotate(degrees, size / 2f, size / 2f)
////        val delta: Float =
////            if (isTop) -(textHeight + mPadding * 2) / 2 else (textHeight + mPadding * 2) / 2
////        val textBaseY: Float = size / 2 - (mTextPaint.descent() + mTextPaint.ascent()) / 2 + delta
////        canvas.drawText(
////            if (mTextAllCaps) mTextContent.toUpperCase() else mTextContent,
////            (
////                    getPaddingLeft() + (size - getPaddingLeft() - getPaddingRight()) / 2).toFloat(),
////            textBaseY,
////            mTextPaint
////        )
////        canvas.restore()
////    }
////
////    private fun drawTextWhenFill(size: Int, degrees: Float, canvas: Canvas, isTop: Boolean) {
////        canvas.save()
////        canvas.rotate(degrees, size / 2f, size / 2f)
////        val delta = if (isTop) (-size / 4).toFloat() else size / 4.toFloat()
////        val textBaseY: Float = size / 2 - (mTextPaint.descent() + mTextPaint.ascent()) / 2 + delta
////        canvas.drawText(
////            if (mTextAllCaps) mTextContent.toUpperCase() else mTextContent,
////            (
////                    getPaddingLeft() + (size - getPaddingLeft() - getPaddingRight()) / 2).toFloat(),
////            textBaseY,
////            mTextPaint
////        )
////        canvas.restore()
////    }
//}