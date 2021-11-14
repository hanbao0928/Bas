/**
 * Created by Lucio on 2021/9/16.
 */
package com.bas.core.lang


/**
 * 两者内容是否相同
 */
inline fun CharSequence?.isContentEquals(other:CharSequence?):Boolean = StringUtils.areContentsSame(this,other)

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




