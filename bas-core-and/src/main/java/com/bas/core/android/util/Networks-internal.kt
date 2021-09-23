/**
 * Created by Lucio on 2020-03-10.
 */
@file:JvmName("Networks")
@file:JvmMultifileClass

package com.bas.core.android.util

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission

@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
@NetworkType
internal fun Context.getNetworkTypeDefault(): Int {
    val ani = connectivityManager?.activeNetworkInfo ?: return NETWORK_TYPE_NONE
    if (!ani.isAvailable)
        return NETWORK_TYPE_NONE
    return when (ani.type) {
        -1 -> NETWORK_TYPE_WIFI
        ConnectivityManager.TYPE_MOBILE -> {
            NETWORK_TYPE_MOBILE
        }
        ConnectivityManager.TYPE_WIFI ->
            NETWORK_TYPE_WIFI

        ConnectivityManager.TYPE_ETHERNET -> {
            NETWORK_TYPE_ETHERNET
        }
        else -> NETWORK_TYPE_UNKNOWN
    }
}

@RequiresApi(23)
@NetworkType
@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
internal fun Context.getNetworkType23(): Int {
    val cm = connectivityManager ?: return NETWORK_TYPE_NONE
    val an = connectivityManager?.activeNetwork ?: return NETWORK_TYPE_NONE
    val nc = cm.getNetworkCapabilities(an) ?: return NETWORK_TYPE_NONE
    return if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
        NETWORK_TYPE_WIFI
    } else if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
        NETWORK_TYPE_MOBILE
    } else if (nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
        NETWORK_TYPE_ETHERNET
    } else {
        NETWORK_TYPE_UNKNOWN
    }
}

/**
 * 转换常量值
 */
internal fun transformNetworkType(@NetworkType type: Int): Int {
    if (Build.VERSION.SDK_INT < 23) {
        return when (type) {
            NETWORK_TYPE_MOBILE -> {
                ConnectivityManager.TYPE_MOBILE
            }
            NETWORK_TYPE_WIFI -> {
                ConnectivityManager.TYPE_WIFI
            }
            NETWORK_TYPE_ETHERNET ->{
                ConnectivityManager.TYPE_ETHERNET
            }
            else -> {
                throw IllegalArgumentException("不支持的NetworkType类型")
            }
        }
    } else {
        return when (type) {
            NETWORK_TYPE_MOBILE -> {
                NetworkCapabilities.TRANSPORT_CELLULAR
            }
            NETWORK_TYPE_WIFI -> {
                NetworkCapabilities.TRANSPORT_WIFI
            }
            NETWORK_TYPE_ETHERNET ->{
                NetworkCapabilities.TRANSPORT_ETHERNET
            }
            else -> {
                throw IllegalArgumentException("不支持的NetworkType类型")
            }
        }
    }
}

@RequiresPermission(value = Manifest.permission.ACCESS_NETWORK_STATE)
internal fun isNetworkConnected(ctx: Context, @NetworkType type: Int): Boolean {
    val cm = ctx.connectivityManager ?: return false
    val netType = transformNetworkType(type)

    if (Build.VERSION.SDK_INT < 23) {
        if (Build.VERSION.SDK_INT >= 21) {
            cm.allNetworks.forEach {
                val networkInfo = cm.getNetworkInfo(it)
                if (networkInfo != null && networkInfo.isConnected && networkInfo.type == netType)
                    return true
            }
        } else {
            val networkInfo = cm.activeNetworkInfo
            return networkInfo != null && networkInfo.isAvailable && networkInfo.type == netType
        }
        return false
    } else {
        val an = cm.activeNetwork ?: return false
        val nc = cm.getNetworkCapabilities(an) ?: return false
        return nc.hasTransport(netType)
    }
}
