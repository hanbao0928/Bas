package bas.android.core.util

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.webkit.MimeTypeMap
import androidx.annotation.ColorInt
import bas.lang.StringUtils
import bas.lang.getExtension

/**
 * Created by Lucio on 2021/12/5.
 */


/**
 * 高亮显示
 * @param start 渲染开始位置
 * @param end 渲染结束位置
 * @param color 颜色
 */
fun CharSequence.toHighLight(start: Int, end: Int, @ColorInt color: Int): CharSequence {
    val style = SpannableStringBuilder(this)
    if (start >= 0 && end >= 0 && end >= start) {
        style.setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return style

}

/**
 * 高亮显示
 * @param tag 需要高亮的部分
 * @param color 渲染颜色
 * @return
 */
fun CharSequence.toHighLight(tag: String, @ColorInt color: Int): CharSequence {
    if (tag.isEmpty() || this.isEmpty())
        return this
    val start = this.indexOf(tag)
    val end = start + tag.length
    return this.toHighLight(start, end, color)
}

/**
 * Returns whether the given [CharSequence] contains only digits.
 *
 * @see TextUtils.isDigitsOnly
 */
inline fun CharSequence.isDigitsOnly() = StringUtils.isDigitsOnly(this)

/**
 * 获取mime type
 */
@JvmOverloads
fun String.getMimeType(defValue: String = "file/*"): String {
    val suffix = this.getExtension()
    if (suffix.isEmpty())
        return defValue
    val type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix)
    if (!type.isNullOrEmpty()) {
        return type
    }
    return defValue
}


