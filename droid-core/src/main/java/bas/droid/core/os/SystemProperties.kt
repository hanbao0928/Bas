/**
 * Created by Lucio on 2021/9/16.
 *
 * 系统属性读取相关
 */
@file:JvmName("SystemPropertiesKt")

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
fun getSerialNO(defVal: String = UNKNOWN_PROPERTY): String {
    try {
        var value = Build.SERIAL
        if (value.isNullOrEmpty() || value == Build.UNKNOWN) {
            value = if (Build.VERSION.SDK_INT >= 26 && ContextCompat.checkSelfPermission(
                    ctxBas,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Build.getSerial()
            } else {
                getSystemProperty(PROP_NAME_SERIALNO, defVal)
            }
        }
        if (!value.isNullOrEmpty() && value != Build.UNKNOWN)
            return value
        return defVal
    } catch (e: Throwable) {
        Logger.e(TAG, "get serialno error:${e.message}", e)
        e.printStackTrace()
        return defVal
    }

}

/**
 * 读取系统变量：优先通过反射读取->命令形式->读取配置文件流
 * @param defVal 默认值
 */
fun getSystemProperty(propName: String, defVal: String = UNKNOWN_PROPERTY): String {
    var value = getSystemPropertyByReflect(propName, defVal)
    if (!value.isInvalidPropertyValue(defVal)) {
        return value
    }

    value = getSystemPropertyByShell(propName, defVal)
    if (!value.isInvalidPropertyValue(defVal)) {
        return value
    }
    value = getSystemPropertyByStream(propName, defVal)
    if (!value.isInvalidPropertyValue(defVal)) {
        return value
    }
    Logger.i(TAG, "getSystemProperty(propName=$propName defVal=$defVal) = $value")
    return value.orDefaultIfNullOrEmpty(defVal)
}


/**
 * 通过脚本读取属性：通过[Runtime]执行命令读取属性
 * @param propName The Property to retrieve
 * @return The Property, or [defVal] if not found
 * 此方法参考[https://searchcode.com/codesearch/view/41537878/]
 */
fun getSystemPropertyByShell(propName: String, defVal: String = UNKNOWN_PROPERTY): String {
    val signature = "getPropertyByRuntime(propName=$propName defVal=$defVal)"
    Logger.d(TAG, signature)
    val sb = StringBuilder()
    try {
        val p = Runtime.getRuntime().exec("getprop $propName")
        BufferedReader(InputStreamReader(p.inputStream), 10240).use { stream ->
            var temp: String? = null
            do {
                temp = stream.readLine()
                if (temp != null) {
                    sb.append(temp)
                }
            } while (temp != null)
        }
        val value = sb.toString().orDefaultIfNullOrEmpty(defVal)
        Logger.i(TAG, "$signature = $value")
        return value
    } catch (e: Throwable) {
        Logger.e(TAG, "$signature failed,return default value(=$defVal)", e)
        return defVal
    }
}

/**
 * 从文件流中读取：读取系统配置文件
 */
fun getSystemPropertyByStream(propName: String, defVal: String = UNKNOWN_PROPERTY): String {
    val signature = "getSystemPropertyByStream(propName=$propName defVal=$defVal)"
    return try {
        Logger.d(TAG, signature)
        val prop = Properties()
        FileInputStream(File(Environment.getRootDirectory(), "build.prop")).use { fis ->
            prop.load(fis)
        }
        val value = prop.getProperty(propName, defVal).orDefaultIfNullOrEmpty(UNKNOWN_PROPERTY)
        Logger.i(TAG, "$signature = $value")
        value
    } catch (e: Throwable) {
        defVal
    }
}

/**
 * 通过反射读取：
 * 反射[android.os.SystemProperties]的get方法读取属性
 */
@SuppressLint("PrivateApi")
fun getSystemPropertyByReflect(propName: String, defVal: String = UNKNOWN_PROPERTY): String {
    val signature = "getPropertyBySystemProperties(propName=$propName defVal=$defVal)"
    try {
        Logger.d(TAG, signature)
        val cls = Class.forName("android.os.SystemProperties")
        val method = cls.getMethod("get", String::class.java, String::class.java)
        val value = method.invoke(cls, propName, defVal) as? String
        Logger.i(TAG, "$signature = $value")
        return value ?: defVal
    } catch (e: Throwable) {
        e.printStackTrace()
        Logger.e(TAG, "$signature failed,default value(=$defVal)", e)
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
    } catch (e: Throwable) {
        Logger.e(TAG, "getPropertyByBuild(key=$propName defVal=$defVal)", e)
        e.printStackTrace()
        return defVal
    }
}

private const val TAG = "SystemProperties"


/**
 * 是否是不可用的属性值
 */
inline fun String?.isInvalidPropertyValue(defVal: String = UNKNOWN_PROPERTY): Boolean {
    return isNullOrEmpty() || this == defVal || this == Build.UNKNOWN
}