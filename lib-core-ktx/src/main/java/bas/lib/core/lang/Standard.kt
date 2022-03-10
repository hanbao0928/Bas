/**
 * Created by Lucio on 2021/9/25.
 */
package bas.lib.core.lang


inline fun <T> T?.orDefault(default: T): T = this ?: default

inline fun <T> T?.orDefault(initializer: () -> T): T = this ?: initializer()

inline fun <T, R> T.mapAs(transformer: T.() -> R): R {
    return transformer(this)
}

/**
 * 此方法看起来与if(){}没有差别，但是此方法可以实现链式调用，而if不能
 */
inline fun <T> T.applyWhen(condition: Boolean, block: T.() -> Unit): T {
    if (condition) {
        return this.apply(block)
    }
    return this
}