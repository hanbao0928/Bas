package bas.android.view.label

import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import androidx.annotation.IntRange
import androidx.annotation.Px
import kotlin.math.*

class LabelViewHelper(val view: View, attrs: AttributeSet? = null, defStyleAttr: Int = 0) {

    var location: Int = DEFAULT_LOCATION
        set(@Location value) {
            if (field == value)
                return
            if (value < LEFT_TOP || value > LEFT_BOTTOM)
                throw IllegalArgumentException("不正确的location参数，只支持LEFT_TOP、RIGHT_TOP、RIGHT_BOTTOM、LEFT_BOTTOM")
            field = value
            view.invalidate()
        }

    var distance: Int = dp(DEFAULT_DISTANCE)
        set(@Px value) {
            if (field == value)
                return
            field = value
            view.invalidate()
        }

    var backgroundColor: Int = DEFAULT_BACKGROUND_COLOR
        set(@ColorInt value) {
            if (field == value)
                return
            field = value
            textRectPaint.color = value
            view.invalidate()
        }

    //文本上下padding
    var padding: Int = dp(DEFAULT_PADDING)
        set(@Px value) {
            if (field == value)
                return
            field = value
            resetTextHeight()
            view.invalidate()
        }

    /*边框*/
    var strokeWidth: Int = 0
        set(@Px value) {
            if (field == value)
                return
            field = value
            this.strokePaint.strokeWidth = value.toFloat()
            view.invalidate()
        }

    /*边框颜色*/
    var strokeColor: Int = DEFAULT_STROKE_COLOR
        set(@ColorInt value) {
            if (field == value)
                return
            field = value
            this.strokePaint.color = value
            view.invalidate()
        }

    var text: String? = null
        set(value) {
            if (field == value)
                return
            field = value
            view.invalidate()
        }

    var textSize: Int = sp(DEFAULT_TEXT_SIZE)
        set(@Px value) {
            if (field == value)
                return
            field = value
            this.textPaint.textSize = value.toFloat()
            resetTextHeight()
            view.invalidate()
        }

    var textColor: Int = DEFAULT_TEXT_COLOR
        set(@ColorInt value) {
            if (field == value)
                return
            field = value
            this.textPaint.color = value
            view.invalidate()
        }

    var textStyle: Int = DEFAULT_TEXT_STYLE
        set(@TextStyle value) {
            if (field == value)
                return
            field = value
            this.textPaint.typeface = Typeface.defaultFromStyle(value)
            view.invalidate()
        }

    var yAxisDegree: Int = DEFAULT_YAXIS_DEGREE
        set(@IntRange(from = 0, to = 90) value) {
            if (field == value)
                return

            field = value
            degreeSin = sin(Math.toRadians(value.toDouble())).toFloat()
            degreeCos = cos(Math.toRadians(value.toDouble())).toFloat()
            view.invalidate()
        }

    //是否可见
    var isVisible: Boolean = true
        set(value) {
            if (field == value)
                return
            field = value
            view.invalidate()
        }

    private val textRectPaint: Paint = Paint().apply {
        isDither = true
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val textPaint: Paint = TextPaint().apply {
        isDither = true
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.SQUARE
    }

    private val strokePaint: Paint = Paint().apply {
        isDither = true
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    /*整个文本框高度：文本+上下padding*/
    private var textRectHeight: Int = 0

    /*文本基线*/
    private var textBaseline: Float = 0f

    /*文本绘制路径*/
    private val textPath: Path = Path()

    /*文本路径长度*/
    private var textPathSideLength: Int = 0

    /*背景绘制路径*/
    private val rectPath: Path = Path()

    /*过渡容器，存储文本测量数据*/
    private val textBound: Rect = Rect()

    //斜边高度：顶点到文本框外边之间的距离
    private var outerDistance: Int = 0

    /*角度正弦值*/
    private var degreeSin: Float = 1f

    /*角度余弦值*/
    private var degreeCos: Float = 1f

    init {
        val ta =
            view.context.obtainStyledAttributes(attrs, R.styleable.LabelView, defStyleAttr, 0)
        location = ta.getInteger(R.styleable.LabelView_label_location, location)
        distance = ta.getDimensionPixelSize(R.styleable.LabelView_label_distance, distance)
        backgroundColor = ta.getColor(R.styleable.LabelView_label_backgroundColor, backgroundColor)
        padding = ta.getDimensionPixelSize(R.styleable.LabelView_label_padding, padding)
        strokeWidth = ta.getDimensionPixelSize(R.styleable.LabelView_label_strokeWidth, strokeWidth)
        strokeColor = ta.getColor(R.styleable.LabelView_label_strokeColor, strokeColor)
        text = ta.getString(R.styleable.LabelView_label_text)
        textSize = ta.getDimensionPixelSize(R.styleable.LabelView_label_textSize, textSize)
        textColor = ta.getColor(R.styleable.LabelView_label_textColor, textColor)
        textStyle = ta.getInt(R.styleable.LabelView_label_textStyle, textStyle)
        yAxisDegree = ta.getInt(R.styleable.LabelView_label_yAxisDegree, yAxisDegree)
        isVisible = ta.getBoolean(R.styleable.LabelView_label_visible, isVisible)
        ta.recycle()
        initDynamicParams()
    }

    private fun initDynamicParams() {
        textRectPaint.color = backgroundColor

        textPaint.textSize = textSize.toFloat()
        textPaint.color = textColor
        textPaint.typeface = Typeface.defaultFromStyle(this.textStyle)
        strokePaint.color = strokeColor
        strokePaint.strokeWidth = strokeWidth.toFloat()

        //一定要在设置文本大小之后再设置
        resetTextHeight()

        degreeSin = sin(Math.toRadians(yAxisDegree.toDouble())).toFloat()
        degreeCos = cos(Math.toRadians(yAxisDegree.toDouble())).toFloat()

    }

    /** 确定View宽度大小  */
    fun measureWidth(widthMeasureSpec: Int): Int {
        var result: Int
        val specMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val specSize = View.MeasureSpec.getSize(widthMeasureSpec)
        when (specMode) {
            View.MeasureSpec.EXACTLY -> {
                //大小确定直接使用
                result = specSize
            }
            View.MeasureSpec.AT_MOST -> {
                /*外层三角形横边长度*/
                val suggestWidth = (outerDistance * 4) / sqrt(2f)
                val sideLength = (outerDistance / degreeCos).toInt()
                result = min(suggestWidth.toInt(), specSize)
                result = max(sideLength, result)

            }
            else -> {
                result = View.getDefaultSize(view.minimumWidth, widthMeasureSpec)
            }
        }
        return result
    }

    /** 确定View宽度大小  */
    fun measureHeight(heightMeasureSpec: Int): Int {
        var result: Int
        val specMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val specSize = View.MeasureSpec.getSize(heightMeasureSpec)
        when (specMode) {
            View.MeasureSpec.EXACTLY -> {
                //大小确定直接使用
                result = specSize
            }
            View.MeasureSpec.AT_MOST -> {
                val suggestHeight = (outerDistance * 4) / sqrt(2f)
                /*外层三角形横边长度*/
                val sideLength = (outerDistance / degreeSin).toInt()
                result = min(suggestHeight.toInt(), specSize)
                result = max(sideLength, result)
            }
            else -> {
                result = View.getDefaultSize(view.minimumWidth, heightMeasureSpec)
            }
        }
        return result
    }

    fun onDraw(canvas: Canvas) {
        val content = this.text
        if (!isVisible || content.isNullOrEmpty()) {
            return
        }
        /*绘制背景*/
        canvas.drawPath(rectPath, textRectPaint)
        /*绘制边框*/
        if (this.strokeWidth > 0) {
            canvas.drawPath(rectPath, strokePaint)
        }

        textPaint.getTextBounds(content, 0, content.length, textBound)
        Log.i(
            "drawTextOnPath",
            "onDraw: textPathSideLength=${textPathSideLength} textBound.width()=${textBound.width()}"
        )
        var pathXOffset = (textPathSideLength - textBound.width()) / 2f
        if (pathXOffset < 0) pathXOffset = 0f
        Log.i("drawTextOnPath", "pathXOffset:$pathXOffset")
        canvas.drawTextOnPath(
            content, textPath,
            pathXOffset,//begin_w_offset,
            0f,//(textBound.height() / 2).toFloat() + textBaseline,
            textPaint
        )
    }

    fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w == oldw && h == oldh)
            return
        rectPath.reset()
        textPath.reset()

        /*小三角形横边长度*/
        val innerXSideLength = distance / degreeCos
        /*小三角形竖边长度*/
        val innerYSideLength = distance / degreeSin

        /*外层三角形横边长度*/
        val outerXSideLength = outerDistance / degreeCos
        /*外层三角形竖边长度*/
        val outerYSideLength = outerDistance / degreeSin

        /*X轴和Y轴的偏移量*/
        var xAxisOffset = 0
        var yAxisOffset = 0
        /*X轴和Y轴计算量方向：正1表示位置正向增大，负一表示点往坐标系反方向移动*/
        var xAxisDirection = 1
        var yAxisDirection = 1
        var textPathDirection = 1

        when (location) {
            RIGHT_TOP -> {
                xAxisOffset = w
                yAxisOffset = 0
                xAxisDirection = -1
                yAxisDirection = 1
                textPathDirection = -1
            }
            LEFT_BOTTOM -> {
                xAxisOffset = 0
                yAxisOffset = h
                xAxisDirection = 1
                yAxisDirection = -1
                textPathDirection = 1
            }
            RIGHT_BOTTOM -> {
                xAxisOffset = w
                yAxisOffset = h
                xAxisDirection = -1
                yAxisDirection = -1
                textPathDirection = -1
            }
            else -> {
                xAxisOffset = 0
                yAxisOffset = 0
                xAxisDirection = 1
                yAxisDirection = 1
                textPathDirection = 1
            }
        }

        rectPath.moveTo(xAxisOffset.toFloat(), yAxisOffset + innerYSideLength * yAxisDirection)
        rectPath.lineTo(xAxisOffset.toFloat(), yAxisOffset + outerYSideLength * yAxisDirection)
        rectPath.lineTo(xAxisOffset + outerXSideLength * xAxisDirection, yAxisOffset.toFloat())
        rectPath.lineTo(xAxisOffset + innerXSideLength * xAxisDirection, yAxisOffset.toFloat())
        rectPath.close()

        /*文本绘制距离顶点的距离：注意baseline的方向，下面的应该是贴近短边，上面的是贴近长边*/
        val textDistance = distance + textRectHeight / 2 + textBaseline * yAxisDirection
        val textPathXSideLength = textDistance / degreeCos
        val textPathYSideLength = textDistance / degreeSin

        val textStartX = xAxisOffset.toFloat()
        val textStartY = yAxisOffset + textPathYSideLength * yAxisDirection
        val textEndX = xAxisOffset + textPathXSideLength * xAxisDirection
        val textEndY = yAxisOffset.toFloat()

        if (textPathDirection < 0) {
            textPath.moveTo(textEndX, textEndY)
            textPath.lineTo(textStartX, textStartY)
        } else {
            textPath.moveTo(textStartX, textStartY)
            textPath.lineTo(textEndX, textEndY)
        }
        textPath.close()

        textPathSideLength = (textPathXSideLength / degreeSin).toInt()
    }

    //重置文本高度
    private fun resetTextHeight() {
        val fontMetrics = textPaint.fontMetrics
        val textHeight: Float = fontMetrics.bottom - fontMetrics.top
        textRectHeight = (textHeight + padding * 2).toInt()
        textBaseline = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
        outerDistance = distance + textRectHeight
    }

    private fun dp(dip: Float): Int {
        return (dip * view.resources.displayMetrics.density + 0.5f).toInt()
    }

    private fun sp(value: Float): Int {
        return (value * view.resources.displayMetrics.scaledDensity + 0.5f).toInt()
    }

    /**
     * 标签位置
     * @see LEFT_TOP 左上角
     * @see RIGHT_TOP 右上角
     * @see RIGHT_BOTTOM 右下角
     * @see LEFT_BOTTOM 左下角
     */
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(value = [LEFT_TOP, RIGHT_TOP, RIGHT_BOTTOM, LEFT_BOTTOM])
    annotation class Location

    @IntDef(value = [Typeface.NORMAL, Typeface.BOLD, Typeface.ITALIC, Typeface.BOLD_ITALIC])
    @Retention(AnnotationRetention.SOURCE)
    annotation class TextStyle

    companion object {
        private const val LEFT_TOP = 0
        private const val RIGHT_TOP = 1
        private const val RIGHT_BOTTOM = 2
        private const val LEFT_BOTTOM = 3

        /*默认位置*/
        private const val DEFAULT_LOCATION = LEFT_TOP

        /*默认距离20dp*/
        private const val DEFAULT_DISTANCE = 20f

        /*默认背景色*/
        private const val DEFAULT_BACKGROUND_COLOR = -0x60d83240

        /*默认文本颜色*/
        private const val DEFAULT_TEXT_COLOR = Color.BLACK

        /*默认文本上下padding dp*/
        private const val DEFAULT_PADDING = 2f
        private const val DEFAULT_TEXT_SIZE = 12f
        private const val DEFAULT_TEXT_STYLE = Typeface.BOLD
        private const val DEFAULT_STROKE_COLOR = Color.BLACK

        /*与Y轴的夹角默认度数*/
        private const val DEFAULT_YAXIS_DEGREE = 45
    }


}