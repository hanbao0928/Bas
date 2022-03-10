/**
 * Created by Lucio on 2021/9/16.
 *
 * 系统属性读取相关
 */
@file:JvmName("SystemProperties")

package bas.droid.core.os

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import bas.droid.core.ctxBas
import bas.droid.core.util.Logger
import bas.lib.core.lang.orDefaultIfNullOrEmpty
import java.io.*
import java.util.*

/**
 * 未知属性
 */
const val UNKNOWN_PROPERTY = Build.UNKNOWN

/**
 * 设备序列号属性
 */
const val PROP_NAME_SERIALNO = "ro.serialno"

/**
 * 获取设备序列号
 */
@SuppressLint("HardwareIds", "MissingPermission")
fun getSerialNO(): String? {
    var value = getSerialNOFromBuildField()
    if (value.isNullOrEmpty()) {
        value = if (Build.VERSION.SDK_INT >= 26 && ContextCompat.checkSelfPermission(
                ctxBas,
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Build.getSerial()
        } else {
            getSystemProperty(PROP_NAME_SERIALNO, "")
        }
    }
    if (value == Build.UNKNOWN)
        value = null
    return value
}

/**
 * 读取系统变量：优先通过命令形式->读取配置文件->反射读取
 * @param defVal 默认值
 */
fun getSystemProperty(propName: String, defVal: String = UNKNOWN_PROPERTY): String {
    var value = getSystemPropertyByShell(propName, defVal)
    if (!value.isInvalidPropertyValue(defVal)) {
        return value
    }

    value = getSystemPropertyByStream(propName, defVal)
    if (!value.isInvalidPropertyValue(defVal)) {
        return value
    }
    value = getSystemPropertyByReflect(propName, defVal)
    if (!value.isInvalidPropertyValue(defVal)) {
        return value
    }
    Logger.i(TAG, "getPropertyAttempt(propName=$propName defVal=$defVal) = $value")
    return value.orDefaultIfNullOrEmpty(defVal)
}

/**
 * 通过脚本读取属性：通过[Runtime]执行命令读取属性
 * @param propName The Property to retrieve
 * @return The Property, or [defVal] if not found
 * 此方法参考[https://searchcode.com/codesearch/view/41537878/]
 */
fun getSystemPropertyByShell(propName: String, defVal: String = UNKNOWN_PROPERTY): String {
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
        try {
            input.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e(
                TAG,
                "getPropertyByRuntime(propName=$propName defVal=$defVal) Close BufferedReader Error",
                e
            )
        }
        val value = line.toString()
        Logger.i(TAG, "getPropertyByRuntime = $value")
        return value.orDefaultIfNullOrEmpty(defVal)
    } catch (e: Exception) {
        Logger.e(TAG, "getPropertyByRuntime(propName=$propName defVal=$defVal)", e)
        return defVal
    } finally {
        try {
            input?.close()
        } catch (e: IOException) {
            Logger.e(TAG, "getPropertyByRuntime Exception while closing InputStream", e)
        }
    }
}

/**
 * 从文件流中读取：读取系统配置文件
 */
fun getSystemPropertyByStream(propName: String, defVal: String = UNKNOWN_PROPERTY): String {
    return try {
        val prop = Properties()
        val `is` = FileInputStream(File(Environment.getRootDirectory(), "build.prop"))
        prop.load(`is`)
        prop.getProperty(propName, defVal)
    } catch (e: Exception) {
        defVal
    }
}

/**
 * 通过反射读取：
 * 反射[android.os.SystemProperties]的get方法读取属性
 */
@SuppressLint("PrivateApi")
fun getSystemPropertyByReflect(propName: String, defVal: String = UNKNOWN_PROPERTY): String {
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
 * 反射[Build]的getString方法读取属性：其本质也是调用的[android.os.SystemProperties]的get方法
 * @see [getSystemPropertyByReflect]
 */
@SuppressLint("DiscouragedPrivateApi")
@Deprecated(
    "使用getSystemPropertyByReflect：反射[Build]的getString方法读取属性：其本质也是调用的[android.os.SystemProperties]的get方法",
    replaceWith = ReplaceWith(
        "getSystemPropertyByReflect(propName,defVal)",
        "bas.droid.core.util.Logger"
    )
)
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

private const val TAG = "SystemProperties"

/**
 * 通过Build读取设备序列号
 */
private fun getSerialNOFromBuildField(): String? {
    val value = Build.SERIAL
    if (value.isNullOrEmpty() || value == Build.UNKNOWN)
        return null
    return value
}

/**
 * 是否是不可用的属性值
 */
internal inline fun String.isInvalidPropertyValue(defVal: String): Boolean {
    return isEmpty() || this == defVal || this == Build.UNKNOWN
}