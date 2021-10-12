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