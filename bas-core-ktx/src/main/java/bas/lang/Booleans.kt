package bas.lang

/**
 * Created by Lucio on 2021/9/16.
 */


inline fun Boolean?.orDefault(def: Boolean = false) = this ?: def