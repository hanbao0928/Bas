package bas.droid.adapter.mediaplayer

import android.Manifest
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission

internal const val TAG = "adapter-mediaplayer"

internal inline fun logi(msg: String) {
    Log.i(TAG, msg)
}

internal inline fun logd(msg: String) {
    Log.d(TAG, msg)
}

internal inline fun logw(msg: String) {
    Log.w(TAG, msg)
}

/**
 * 布局加载服务LayoutInflater
 */
internal inline val Context.layoutInflater: android.view.LayoutInflater
    get() = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as android.view.LayoutInflater

/**
 * 网络服务ConnectivityManager
 */
internal inline val Context.connectivityManager: android.net.ConnectivityManager?
    get() = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? android.net.ConnectivityManager

/**
 * 网络是否连接
 */
@get:RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
internal val Context.isNetworkConnected: Boolean
    get() {
        val cm = connectivityManager ?: return false
        if (Build.VERSION.SDK_INT < 23) {
            return cm.activeNetworkInfo?.isConnected == true
        } else {
            val an = cm.activeNetwork ?: return false
            return cm.getNetworkCapabilities(an) != null
        }
    }