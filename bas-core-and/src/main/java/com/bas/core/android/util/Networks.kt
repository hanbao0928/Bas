/**
 * Created by Lucio on 2020-03-10.
 */
@file:JvmName("Networks")
@file:JvmMultifileClass

package com.bas.core.android.util

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.annotation.IntDef
import androidx.annotation.RequiresPermission
import com.bas.core.lang.orDefault

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