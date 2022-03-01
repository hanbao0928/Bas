/**
 * Created by Lucio on 2021/9/25.
 */
package bas.lang


inline fun <T> T?.orDefault(default: T): T = this ?: default

inline fun <T> T?.orDefault(initializer: () -> T): T = this ?: initializer()

inline fun <T, R> T.mapAs(transformer: T.() -> R): R {
    return transformer(this)
}