package com.bas.adapter.imageloader

import android.util.Log
import com.bas.adapter.imageloader.glide.GlideEngine

/**
 * Created by Lucio on 2021/11/13.
 */

internal var engine: ImageLoaderEngine = GlideEngine

internal const val DEBUG = true
internal const val TAG = "BasImageLoader"

internal fun logi(msg: String) {
    Log.i(TAG, msg)
}

internal fun logw(msg: String) {
    Log.w(TAG, msg)
}
