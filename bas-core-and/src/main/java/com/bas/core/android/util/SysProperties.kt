/**
 * Created by Lucio on 2021/9/16.
 */
@file:JvmName("SysProperties")

package com.bas.core.android.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.bas.core.android.basCtx
import com.bas.core.lang.orDefaultIfNullOrEmpty
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

const val UNKNOWN_PROPERTY = Build.UNKNOWN

/**
 * 设备序列号属性
 */
const val PROP_NAME_SERIALNO = "ro.serialno"

private const val TAG = "SystemProperties"

/**
 * 反射[Build]的getString方法读取属性：其本质也是调用的[android.os.SystemProperties]的get方法
 * @see [getPropertyBySystemProperties]
 */
@SuppressLint("DiscouragedPrivateApi")
fun getPropertyByBuild(propName: String, defVal: String = UNKNOWN_PROPERTY): String {
    try {
        Logger.i(TAG, "getPropertyByBuild(propName=$propName defVal=$defVal)")
        val cls = Class.forName("android.os.Build")
        val method = cls.getDeclaredMethod("getString", String::class.java)
        method.isAccessible = true
        val value = method.invoke(null, propName) as? String
        Logger.i(TAG, "getPropertyByBuild = $value")
        return if (value.isNullOrEmpty() || value == Build.UNKNOWN) {
            defVal
        } else {
            value
        }
    } catch (e: Exception) {
        Logger.e(TAG, "getPropertyByBuild(key=$propName defVal=$defVal)", e)
        e.printStackTrace()
        return defVal
    }
}

/**
 * 反射[android.os.SystemProperties]的get方法读取属性
 */
@SuppressLint("PrivateApi")
fun getPropertyBySystemProperties(propName: String, defVal: String = UNKNOWN_PROPERTY): String {
    try {
        Logger.i(TAG, "getPropertyBySystemProperties(propName=$propName defVal=$defVal)")
        val cls = Class.forName("android.os.SystemProperties")
        val method = cls.getMethod("get", String::class.java, String::class.java)
        val value = method.invoke(cls, propName, defVal) as? String
        Logger.i(TAG, "getPropertyBySystemProperties = $value")
        return value ?: defVal
    } catch (e: Exception) {
        e.printStackTrace()
        Logger.e(TAG, "getPropertyBySystemProperties(key=$propName defVal=$defVal)", e)
        return defVal
    }
}

/**
 * 通过[Runtime]执行命令读取属性
 * @param propName The Property to retrieve
 * @return The Property, or [defVal] if not found
 * 此方法参考[https://searchcode.com/codesearch/view/41537878/]
 */
fun getPropertyByRuntime(propName: String, defVal: String = UNKNOWN_PROPERTY): String {
    Logger.i(TAG, "getPropertyByRuntime(propName=$propName defVal=$defVal)")
    val line = java.lang.StringBuilder()
    var input: BufferedReader? = null
    try {
        val p = Runtime.getRuntime().exec("getprop $propName")
        input = BufferedReader(InputStreamReader(p.inputStream), 10240)
        var temp: String? = null
        do {
            temp = input.readLine()
            if (temp != null) {
                line.append(temp)
            }
        } while (temp != null)
        try{
            input.close()
        }catch (e:Exception){
            e.printStackTrace()
            Logger.e(TAG, "getPropertyByRuntime(propName=$propName defVal=$defVal) Close BufferedReader Error", e)
        }
        val value = line.toString()
        Logger.i(TAG, "getPropertyByRuntime = $value")
        return value.orDefaultIfNullOrEmpty(defVal)
    } catch (e: Exception) {
        Logger.e(TAG, "getPropertyByRuntime(propName=$propName defVal=$defVal)", e)
        return defVal
    } finally {
        if (input != null) {
            try {
                input.close()
            } catch (e: IOException) {
                Logger.e(TAG, "getPropertyByRuntime Exception while closing InputStream", e)
            }
        }
    }
}

 inline fun String.isInvalidPropertyValue(defVal: String): Boolean {
    return isEmpty() || this == defVal || this == Build.UNKNOWN
}

/**
 * 尝试获取属性
 */
fun getPropertyAttempt(propName: String, defVal: String = UNKNOWN_PROPERTY): String {
    var value = getPropertyByBuild(propName, defVal)
    if (value.isInvalidPropertyValue(defVal)) {
        value = getPropertyBySystemProperties(propName, defVal)
    }
    if (value.isInvalidPropertyValue(defVal)) {
        value = getPropertyByRuntime(propName, defVal)
    }
    Logger.i(TAG, "getPropertyAttempt(propName=$propName defVal=$defVal) = $value")
    return value
}

/**
 * 获取设备序列号
 */
@SuppressLint("HardwareIds", "MissingPermission")
fun getSerialNO(): String? {
    var value = getSerialNOFromBuildField()
    if (value.isNullOrEmpty()) {
        value = if (Build.VERSION.SDK_INT >= 26 && ContextCompat.checkSelfPermission(
                basCtx,
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Build.getSerial()
        } else {
            getPropertyAttempt(PROP_NAME_SERIALNO, "")
        }
    }
    if (value == Build.UNKNOWN)
        value = null
    return value
}

/**
 * 通过Build读取设备序列号
 */
private fun getSerialNOFromBuildField(): String? {
    val value = Build.SERIAL
    if (value.isNullOrEmpty() || value == Build.UNKNOWN)
        return null
    return value
}

