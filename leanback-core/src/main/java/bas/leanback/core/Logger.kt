package bas.leanback.core

import android.util.Log

/**
 * Created by Lucio on 2021/11/17.
 */

private const val TAG = "LeanbackCore"
private const val DEBUG = true

internal inline fun logd(msg: String) {
    if (!DEBUG)
        return
    Log.d(TAG, msg)
}

internal inline fun logi(msg: String) {
    Log.i(TAG, msg)
}

@JvmOverloads
internal inline fun logw(msg: String, e: Throwable? = null) {
    Log.w(TAG, msg, e)
}

@JvmOverloads
internal inline fun loge(msg: String, e: Throwable? = null) {
    Log.e(TAG, msg, e)
}
