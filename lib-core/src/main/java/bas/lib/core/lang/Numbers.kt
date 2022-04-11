@file:JvmName("NumbersKt")

package bas.lib.core.lang

import java.text.DecimalFormat


/**
 * Created by Lucio on 2021/9/30.
 */


inline fun Int?.orDefault(def: Int = 0) = this ?: def
inline fun Float?.orDefault(def: Float = 0f) = this ?: def
inline fun Long?.orDefault(def: Long = 0) = this ?: def
inline fun Double?.orDefault(def: Double = 0.0) = this ?: def


inline fun Int?.avoidZeroDividend(): Int = if (this == null || this == 0) 1 else this
inline fun Float?.avoidZeroDividend(): Float = if (this == null || this == 0f) 1f else this
inline fun Long?.avoidZeroDividend(): Long = if (this == null || this == 0L) 1L else this
inline fun Double?.avoidZeroDividend(): Double = if (this == null || this == 0.0) 1.0 else this

/**
 * 保留一位小数
 */
inline fun Double?.toDecimal1(): String {
    return NumberUtils.toDecimal1(this.orDefault())
}

/**
 * 保留为两位小数
 */
inline fun Double?.toDecimal2(): String {
    return NumberUtils.toDecimal2(this.orDefault())
}

/**
 * 保留小数位
 * @param decimalPlaceCnt 小数位个数
 */
inline fun Double?.toDecimal(decimalPlaceCnt: Int = 1): String {
    return NumberUtils.toDecimal(this.orDefault(), decimalPlaceCnt)
}

/**
 * 保留一位小数
 */
inline fun Float?.toDecimal1(): String {
    return NumberUtils.toDecimal1(this.orDefault())
}

/**
 * 保留为两位小数
 */
inline fun Float?.toDecimal2(): String {
    return NumberUtils.toDecimal2(this.orDefault())
}

/**
 * 保留小数位
 * @param decimalPlaceCnt 小数位个数
 */
inline fun Float?.toDecimal(decimalPlaceCnt: Int = 1): String {
    return NumberUtils.toDecimal(this.orDefault(), decimalPlaceCnt)
}

@JvmOverloads
fun Int.toDataSize(pattern: String = "###.00"): String = this.toLong().toDataSize(pattern)

@JvmOverloads
fun Double.toDataSize(pattern: String = "###.00"): String = this.toLong().toDataSize(pattern)

@JvmOverloads
fun Float.toDataSize(pattern: String = "###.00"): String = this.toLong().toDataSize(pattern)

/**
 * 格式化数据显示
 */
@JvmOverloads
fun Long.toDataSize(pattern: String = "###.00"): String {
    val format = DecimalFormat(pattern)
    return when {
        this < 0 -> "-"
        this < 1024 -> this.toString() + "B"
        this < 1024 * 1024 -> format.format((this / 1024f).toDouble()) + "KB"
        this < 1024 * 1024 * 1024 -> format.format((this.toFloat() / 1024f / 1024f).toDouble()) + "MB"
        else -> format.format((this.toFloat() / 1024f / 1024f / 1024f).toDouble()) + "GB"
    }
}
