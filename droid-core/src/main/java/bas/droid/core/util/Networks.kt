/**
 * Created by Lucio on 2020-03-10.
 */
@file:JvmName("NetworksKt")
@file:JvmMultifileClass

package bas.droid.core.util

import android.Manifest
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.IntDef
import androidx.annotation.RequiresPermission
import bas.lib.core.lang.orDefault

/**
 * 无网络
 */
const val NETWORK_TYPE_NONE = -1

/**
 * 手机网络
 */
const val NETWORK_TYPE_MOBILE = 1

/**
 * WIFI
 */
const val NETWORK_TYPE_WIFI = 2

/**
 * 未知网络
 */
const val NETWORK_TYPE_UNKNOWN = 3

/**
 * 有线
 */
const val NETWORK_TYPE_ETHERNET = 9

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    flag = true,
    value = [NETWORK_TYPE_NONE, NETWORK_TYPE_WIFI, NETWORK_TYPE_MOBILE, NETWORK_TYPE_UNKNOWN]
)
annotation class NetworkType

/**
 * 获取网络类型
 */
@NetworkType
@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.getNetworkType(): Int {
    val cm = connectivityManager ?: return NETWORK_TYPE_NONE
    if (Build.VERSION.SDK_INT >= 23) {
        return getNetworkType23()
    } else {
        return getNetworkTypeDefault()
    }
}

/**
 * 网络是否连接
 */
@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isNetworkConnected(): Boolean {
    val cm = connectivityManager ?: return false
    if (Build.VERSION.SDK_INT < 23) {
        return cm.activeNetworkInfo?.isConnected.orDefault()
    } else {
        val an = cm.activeNetwork ?: return false
        return cm.getNetworkCapabilities(an) != null
    }
}

/**
 * wifi 是否连接
 */
@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isWifiConnected(): Boolean {
    return isNetworkConnected(this, NETWORK_TYPE_WIFI)
}

/**
 * 手机信号是否打开
 */
@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isGPRSConnected(): Boolean {
    return isNetworkConnected(this, NETWORK_TYPE_MOBILE)
}

/**
 * 获取Wifi SSID(wifi名字)
 */
@RequiresPermission(allOf = [Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_NETWORK_STATE])
fun Context.getWifiSSID(): String? {
    if (!isWifiConnected())
        return null
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1) {
        val connManager = connectivityManager ?: return null
        val networkInfo = connManager.activeNetworkInfo ?: return null
        if (networkInfo.isConnected) {
            if (networkInfo.extraInfo != null) {
                return networkInfo.extraInfo.replace("\"", "")
            }
        }
        return null
    } else {
        val wifiMgr = applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
            ?: return null
        val ssid = wifiMgr.connectionInfo.ssid
        return if (ssid.isNullOrEmpty()) {
            null
        } else {
            if (ssid.contains("\"")) ssid.replace("\"", "") else ssid
        }
    }
}