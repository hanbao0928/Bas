package com.bas.android.multidex

import android.util.Log

/**
 * Created by Lucio on 2021/11/17.
 */

private const val TAG = "MultiDexCompat"
private const val DEBUG = true

internal fun logd(msg: String) {
    if (!DEBUG)
        return
    Log.d(TAG, msg)
}

internal fun logi(msg: String) {
    Log.i(TAG, msg)
}

@JvmOverloads
internal fun logw(msg: String, e: Throwable? = null) {
    Log.w(TAG, msg, e)
}

@JvmOverloads
internal fun loge(msg: String, e: Throwable? = null) {
    Log.e(TAG, msg, e)
}