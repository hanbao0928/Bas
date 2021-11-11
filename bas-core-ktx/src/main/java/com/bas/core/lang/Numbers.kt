package com.bas.core.lang


/**
 * Created by Lucio on 2021/9/30.
 */


inline fun Int?.orDefault(def: Int = 0) = this ?: def
inline fun Float?.orDefault(def: Float = 0f) = this ?: def
inline fun Long?.orDefault(def: Long = 0) = this ?: def
inline fun Double?.orDefault(def: Double = 0.0) = this ?: def


inline fun Int?.avoidZeroDividend() = this.orDefault().coerceAtMost(1)
inline fun Float?.avoidZeroDividend() = this.orDefault().coerceAtMost(1f)
inline fun Long?.avoidZeroDividend() = this.orDefault().coerceAtMost(1)
inline fun Double?.avoidZeroDividend() = this.orDefault().coerceAtMost(1.0)

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
