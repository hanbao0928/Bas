@file:JvmName("BasCore")
@file:JvmMultifileClass
package bas.android.core

import bas.android.core.util.Logger

/**
 * Created by Lucio on 2021/11/9.
 */

internal var isBasCoreInit: Boolean = false



private const val TAG = "BasCore"
private const val DEBUG = true
internal fun logd(msg: String) {
    if(!DEBUG)
        return
    Logger.d(TAG, msg)
}

internal fun logi(msg: String) {
    if(!DEBUG)
        return
    Logger.i(TAG, msg)
}

internal fun loge(msg: String) {
    if(!DEBUG)
        return
    Logger.e(TAG, msg)
}