
/**
 * Created by Lucio on 2021/7/27.
 */

package com.bas.core.lang


inline fun <T> T.tryIgnore(action: T.() -> Unit): Throwable? {
    return try {
        action(this)
        null
    } catch (e: Throwable) {
        e
    }

}

inline fun <T> Throwable?.onCatch(block: (Throwable) -> T): T? {
    if (this == null)
        return null
    return block(this)
}