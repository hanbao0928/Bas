package com.bas.core.android.app.internal

import com.bas.core.android.util.Logger

/**
 * Created by Lucio on 2021/11/16.
 */

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