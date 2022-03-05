package bas.droid.adapter.imageloader

import android.util.Log
import bas.droid.adapter.imageloader.glide.GlideEngine

/**
 * Created by Lucio on 2021/11/13.
 */

internal var engine: ImageLoaderEngine = GlideEngine

internal const val DEBUG = true
internal const val TAG = "ImageLoaderBas"

internal fun logi(msg: String) {
    Log.i(TAG, msg)
}

internal fun logw(msg: String) {
    Log.w(TAG, msg)
}

internal fun loge(msg: String, e: Throwable? = null) {
    Log.e(TAG, msg, e)
}
