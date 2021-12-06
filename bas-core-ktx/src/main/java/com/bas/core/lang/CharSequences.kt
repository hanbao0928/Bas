/**
 * Created by Lucio on 2021/9/16.
 */
package com.bas.core.lang


inline fun String?.orEmpty(): String = this ?: ""

/**
 * 两者内容是否相同
 */
inline fun CharSequence?.isContentEquals(other: CharSequence?): Boolean =
    StringUtils.areContentsSame(this, other)

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
 * 获取字符串后缀名
 * @param delimiter 分隔符
 * @param missingDelimiterValue 未查找到分隔符时的默认返回值
 */
@JvmOverloads
inline fun String.getExtension(delimiter: String = ".", missingDelimiterValue: String = this) =
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



