package com.bas.core.lang

/**
 * Created by Lucio on 2021/9/25.
 */

inline fun <T> T?.orDefault(initializer: () -> T): T = this ?: initializer()

inline fun <T> T?.orDefault(default:T): T = this ?: default

inline fun <T, R> T.mapAs(transformer: T.() -> R): R {
    return transformer(this)
}





