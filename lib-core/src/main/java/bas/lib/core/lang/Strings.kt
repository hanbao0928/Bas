/**
 * Created by Lucio on 2021/9/16.
 */
@file:JvmName("StringsKt")
@file:JvmMultifileClass

package bas.lib.core.lang
import kotlin.text.isNullOrEmpty as libIsNullOrEmpty

/**
 * for java
 */
inline fun String?.isNullOrEmpty():Boolean = this.libIsNullOrEmpty()

inline fun String?.orEmpty(): String = this ?: ""

/**
 * null或空字符串时使用默认值
 */
inline fun String?.orDefaultIfNullOrEmpty(def: String = ""): String =
    StringUtils.orDefaultIfNullOrEmpty(this, def)

@JvmOverloads
inline fun String?.toLongOrDefault(defaultValue: Long = 0): Long =
    StringUtils.toLongOrDefault(this, defaultValue)

@JvmOverloads
inline fun String?.toIntOrDefault(defaultValue: Int = 0): Int =
    StringUtils.toIntOrDefault(this, defaultValue)

/**
 * 两者内容是否相同
 */
inline fun CharSequence?.isContentEquals(other: CharSequence?): Boolean =
    StringUtils.areContentsSame(this, other)



/**
 * 获取字符串后缀名
 * @param delimiter 分隔符
 * @param missingDelimiterValue 未查找到分隔符时的默认返回值
 * @see getSuffix
 */
@Deprecated("使用getSuffix扩展方法",replaceWith = ReplaceWith("getSuffix"))
@JvmOverloads
inline fun String.getExtension(delimiter: String = ".", missingDelimiterValue: String = this) =getSuffix(delimiter, missingDelimiterValue)

/**
 * 获取字符串后缀名
 * @param delimiter 分隔符
 * @param missingDelimiterValue 未查找到分隔符时的默认返回值
 */
@JvmOverloads
inline fun String.getSuffix(delimiter: String = ".", missingDelimiterValue: String = this) =
    substringAfterLast(delimiter, missingDelimiterValue)

/**
 * 获取名字（不包括后缀）：获取分隔符之前的内容
 * @param delimiter 分隔符
 */
@JvmOverloads
fun String.getName(delimiter: String = ".", missingDelimiterValue: String = this): String =
    substringBeforeLast(delimiter, missingDelimiterValue)

/**
 * 转换成首字母大写
 */
inline fun String?.toFirstLetterUppercase(): String = StringUtils.toFirstLetterUppercase(this)

/**
 * 转换成半拼
 */
inline fun String?.toHalfWidth(): String = StringUtils.toHalfWidth(this)

/**
 * 转换成全拼
 */
inline fun String?.toFullWidth(): String = StringUtils.toFullWidth(this)

/**
 * 左填充
 * @param fill   填充字符串
 * @param total  字符串总长度
 * @return
 */
fun String.padLeft(fill: String, total: Int): String {
    if (this.length >= total) return this
    val sb = StringBuilder()
    for (i in 0 until total - this.length) {
        sb.append(fill)
    }
    sb.append(this)
    return sb.toString()
}


