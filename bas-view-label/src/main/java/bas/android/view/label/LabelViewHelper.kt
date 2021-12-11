package bas.android.view.label

import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IntDef

class LabelViewHelper(val view: View, attrs: AttributeSet? = null, defStyleAttr: Int = 0) {

    var location: Int
        private set
    var text: String?
        private set

    var textSize: Int
        private set


    private var distance: Int

    //文本上下padding
    private var padding: Int

    var backgroundColor: Int
        private set

    var textStyle: Int
        private set

    var textColor: Int
        private set

    /*边框*/
    private var strokeWidth: Int

    /*边框颜色*/
    var strokeColor: Int
        private set


    private var textRectHeight: Int = 0


    private var height: Int


    //是否可见
    var isVisible: Boolean
        private set


    private val textRectPaint: Paint = Paint().apply {
//        isDither = true
//        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val textPaint: Paint = TextPaint().apply {
//        isDither = true
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.SQUARE
    }

    private val strokePaint: Paint = Paint().apply {
//        isDither = true
//        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private val textPath: Path

    // simulator
    private val rectPath: Path
    private val textBound: Rect

    init {
        val ta =
            view.context.obtainStyledAttributes(attrs, R.styleable.LabelView, defStyleAttr, 0)

        location = ta.getInteger(R.styleable.LabelView_label_location, LEFT_TOP)

        distance = ta.getDimensionPixelSize(
            R.styleable.LabelView_label_distance,
            dp(DEFAULT_DISTANCE)
        )

        text = ta.getString(R.styleable.LabelView_label_text)
        textSize = ta.getDimensionPixelSize(
            R.styleable.LabelView_label_textSize,
            sp(DEFAULT_TEXT_SIZE)
        )

        textStyle = ta.getInt(
            R.styleable.LabelView_label_textStyle, DEFAULT_TEXT_STYLE
        )
        textColor = ta.getColor(
            R.styleable.LabelView_label_textColor,
            DEFAULT_TEXT_COLOR
        )

        padding = ta.getDimensionPixelSize(
            R.styleable.LabelView_label_distance,
            dp(DEFAULT_PADDING)
        )

        textPaint.textSize = textSize.toFloat()
        textPaint.color = textColor
        textPaint.typeface = Typeface.defaultFromStyle(this.textStyle)
        resetTextHeight()

        backgroundColor = ta.getColor(
            R.styleable.LabelView_label_backgroundColor,
            DEFAULT_BACKGROUND_COLOR
        )
        textRectPaint.color = backgroundColor


        height = ta.getDimensionPixelSize(
            R.styleable.LabelView_label_height, dp(DEFAULT_HEIGHT.toFloat())
        )
        strokeWidth = ta.getDimensionPixelSize(R.styleable.LabelView_label_strokeWidth, 0)

        strokeColor =
            ta.getColor(R.styleable.LabelView_label_strokeColor, DEFAULT_STROKE_COLOR)

        isVisible = true

        ta.recycle()

        if (strokeWidth > 0) {
            textRectPaint.strokeWidth = strokeWidth.toFloat()
            textRectPaint.color
        }

        strokePaint.color = strokeColor
        strokePaint.strokeWidth = strokeWidth.toFloat()

        rectPath = Path()
        textPath = Path()
        textBound = Rect()
    }


    fun onDraw(canvas: Canvas, measuredWidth: Int, measuredHeight: Int) {
        val content = this.text
        if (!isVisible || content.isNullOrEmpty()) {
            return
        }

        val actualDistance = (distance + height / 2).toFloat()
        updatePath(measuredWidth, measuredHeight)


        canvas.drawPath(rectPath, textRectPaint)
        if(this.strokeWidth > 0){
            canvas.drawPath(rectPath, strokePaint)
        }
        textPaint.getTextBounds(content, 0, content.length, textBound)
        var begin_w_offset = 1.4142135f * actualDistance / 2 - textBound.width() / 2
        if (begin_w_offset < 0) begin_w_offset = 0f
        canvas.drawTextOnPath(
            this.text!!,
            textPath,
            begin_w_offset,
            (textBound.height() / 2).toFloat(),
            textPaint
        )
    }

    private fun updatePath(measuredWidth: Int, measuredHeight: Int) {
        rectPath.reset()
        textPath.reset()
        height = textRectHeight
        val startPosX = (measuredWidth - distance - height).toFloat()
        val endPosX = measuredWidth.toFloat()
        val startPosY = (measuredHeight - distance - height).toFloat()
        val endPosY = measuredHeight.toFloat()
        val middle = (height / 2).toFloat()
        when (location) {
            RIGHT_TOP -> {
                rectPath.moveTo(startPosX, 0f)
                rectPath.lineTo(startPosX + height, 0f)
                rectPath.lineTo(endPosX, distance.toFloat())
                rectPath.lineTo(endPosX, (distance + height).toFloat())
                rectPath.close()
                textPath.moveTo(startPosX + middle, 0f)
                textPath.lineTo(endPosX, distance + middle)
                textPath.close()
            }
            LEFT_BOTTOM -> {
                rectPath.moveTo(0f, startPosY)
                rectPath.lineTo((distance + height).toFloat(), endPosY)
                rectPath.lineTo(distance.toFloat(), endPosY)
                rectPath.lineTo(0f, startPosY + height)
                rectPath.close()
                textPath.moveTo(0f, startPosY + middle)
                textPath.lineTo(distance + middle, endPosY)
                textPath.close()
            }
            RIGHT_BOTTOM -> {
                rectPath.moveTo(startPosX, endPosY)
                rectPath.lineTo(measuredWidth.toFloat(), startPosY)
                rectPath.lineTo(measuredWidth.toFloat(), startPosY + height)
                rectPath.lineTo(startPosX + height, endPosY)
                rectPath.close()
                textPath.moveTo(startPosX + middle, endPosY)
                textPath.lineTo(endPosX, startPosY + middle)
                textPath.close()
            }
            else -> {
                rectPath.moveTo(0f, distance.toFloat())
                rectPath.lineTo(distance.toFloat(), 0f)
                rectPath.lineTo((distance + height).toFloat(), 0f)
                rectPath.lineTo(0f, (distance + height).toFloat())
                rectPath.close()

                textPath.moveTo(0f, distance + middle)
                textPath.lineTo(distance + middle, 0f)
                textPath.close()
            }
        }
    }

    private fun dp(dip: Float): Int {
        return (dip * view.resources.displayMetrics.density + 0.5f).toInt()
    }

    private fun sp(value: Float): Int {
        return (value * view.resources.displayMetrics.scaledDensity + 0.5f).toInt()
    }

    fun setLabelHeight(view: View, height: Int) {
        if (this.height != dp(height.toFloat())) {
            this.height = dp(height.toFloat())
            view.invalidate()
        }
    }



    fun setLabelVisual(view: View, visual: Boolean) {
        if (isVisible != visual) {
            isVisible = visual
            view.invalidate()
        }
    }

    fun setLabelOrientation(view: View, orientation: Int) {
        if (location != orientation && orientation <= 4 && orientation >= 1) {
            location = orientation
            view.invalidate()
        }
    }



    fun setStrokeColor( strokeColor: Int) {
        if (this.strokeColor == strokeColor)
            return
        this.strokeColor = strokeColor
        this.strokePaint.color = strokeColor
        view.invalidate()
    }

    fun setStrokeWidth(strokeWidthPx: Int) {
        if (this.strokeWidth == strokeWidthPx)
            return
        this.strokeWidth = strokeWidthPx
        this.strokePaint.strokeWidth = strokeWidth.toFloat()
        view.invalidate()
    }

    fun setText(text: String?) {
        if (this.text == text)
            return
        this.text = text
        view.invalidate()
    }

    fun setTextColor(textColor: Int) {
        if (this.textColor == textColor)
            return
        this.textColor = textColor
        view.invalidate()
    }

    fun setTextSize(textSize: Int) {
        if (this.textSize == textSize)
            return
        this.textSize = textSize
        this.textPaint.textSize = textSize.toFloat()
        resetTextHeight()
        view.invalidate()
    }

    fun setDistance(distancePx: Int) {
        if (this.distance == distancePx)
            return
        this.distance = distancePx
        view.invalidate()
    }

    fun setBackgroundColor(backgroundColor: Int) {
        if (this.backgroundColor == backgroundColor)
            return
        this.backgroundColor = backgroundColor
        textRectPaint.color = backgroundColor
        view.invalidate()
    }

    //重置文本高度
    private fun resetTextHeight() {
        val textHeight: Float = textPaint.descent() - textPaint.ascent()
        textRectHeight = (textHeight + padding * 2).toInt()
    }

    companion object {


        private const val DEFAULT_HEIGHT = 20


        private const val LEFT_TOP = 0
        private const val RIGHT_TOP = 1
        private const val RIGHT_BOTTOM = 2
        private const val LEFT_BOTTOM = 3

        private const val DEFAULT_TEXT_COLOR = Color.BLACK
        private const val DEFAULT_DISTANCE = 40f
        private const val DEFAULT_TEXT_SIZE = 14f
        private const val DEFAULT_TEXT_STYLE = Typeface.NORMAL


        private const val DEFAULT_BACKGROUND_COLOR = -0x60d83240
        private const val DEFAULT_STROKE_COLOR = Color.BLACK

        /*默认文本上下padding dp*/
        private const val DEFAULT_PADDING = 3.5f
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(LEFT_TOP, RIGHT_TOP, RIGHT_BOTTOM, LEFT_BOTTOM)
    annotation class Location {

    }

}