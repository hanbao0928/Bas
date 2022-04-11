@file:JvmName("SharedPref")
@file:JvmMultifileClass

package bas.droid.core.content

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.IntRange
import java.io.File
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty

@SuppressLint("ApplySharedPref")
inline fun SharedPreferences.edit(
    commit: Boolean = false,
    action: SharedPreferences.Editor.() -> Unit
) {
    val editor = edit()
    action(editor)
    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
}

fun <T> SharedPreferences.get(key: String, default: T): T {
    val result: Any = when (default) {
        is Long -> getLong(key, default)
        is Int -> getInt(key, default)
        is Float -> getFloat(key, default)
        is Boolean -> getBoolean(key, default)
        is String -> getString(key, default).orEmpty()
        else -> throw  IllegalArgumentException("无法识别的数据类型")
    }
    return result as T
}

fun <T> SharedPreferences.put(key: String, value: T) {
    edit {
        when (value) {
            is Long -> putLong(key, value)
            is Int -> putInt(key, value)
            is Float -> putFloat(key, value)
            is Boolean -> putBoolean(key, value)
            is String -> putString(key, value)
            else -> throw  IllegalArgumentException("this type can not be saved into preference")
        }
    }
}

fun <T> Delegates.sharedPreferences(
    provider: SharedPreferencesProvider,
    key: String,
    defaultValue: T
): ReadWriteProperty<Any?, T> {
    return SharedPreferenceProperty<T>(provider, key, defaultValue)
}

fun <T> Delegates.sharedPreferences(
    sp: SharedPreferences, key: String, defaultValue: T
): ReadWriteProperty<Any?, T> {
    return SharedPreferenceProperty<T>(sp, key, defaultValue)
}

/**
 * 重构SharedPreferences，提供更新处理方法
 * @param version 当前版本号，如果当前版本号大于之前的版本号，则会触发[onUpgrade]
 * @param onUpgrade 更新回调的方法
 */
@JvmOverloads
fun Context.getSharedPreferencesBas(
    name: String,
    @IntRange(from = 1, to = Long.MAX_VALUE) version: Long = 1,
    mode: Int = Context.MODE_PRIVATE,
    onUpgrade: OnSharedPrefUpgradeListener? = null
): SharedPreferences {
    return SharedPreferencesBas(this, name, version, mode, onUpgrade)
}

/**
 * 提供动态的SharedPreference（比如根据特定条件创建的sp）
 */
interface SharedPreferencesProvider {
    val pref: SharedPreferences
}

/**
 * SharedPreferences 升级回调
 */
fun interface OnSharedPrefUpgradeListener {
    fun onSharedPrefUpgrade(sp: SharedPreferences, oldVersion: Long, newVersion: Long)
}


/**
 * 删除SharedPreferences文件
 * @param fileName 文件名
 */
fun deleteSharedPreferenceFile(context: Context, fileName: String): Boolean {
    val sb = StringBuilder()
    sb.append(context.applicationContext.filesDir.parent)
        .append("/shared_prefs/")
        .append(fileName)
        .append(".xml")
    val file = File(sb.toString())
    if (file.exists()) {
        return file.delete()
    }
    return true
}
