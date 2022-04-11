package bas.droid.core.graphics

import android.graphics.Paint
import android.text.TextPaint
import androidx.annotation.Px
import kotlin.math.ceil

/**
 * Created by Lucio on 2021/12/11.
 */

val Paint.baseline: Float
    get() {
        val fontMetrics = this.fontMetrics
        return (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
    }


/**
 * 获取文本高度
 */
@Px
fun Paint.getTextHeight(): Int {
    val fm = fontMetrics
    return ceil((fm.descent - fm.ascent).toDouble()).toInt()
}
