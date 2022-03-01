package bas.lang

/**
 * Created by Lucio on 2020-10-29.
 */

inline fun Boolean?.orDefault(def: Boolean = false) = this ?: def