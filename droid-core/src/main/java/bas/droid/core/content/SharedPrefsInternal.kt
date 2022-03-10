@file:JvmName("SharedPref")
@file:JvmMultifileClass
package bas.droid.core.content

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Lucio on 2020-03-10.
 */
internal const val SHARED_PREF_VERSION_CODE = "_am_sp_version_code"

/**
 * 实现可以检查更新的SharedPreferences
 * @param name 文件名字
 * @param mode
 */
internal class SharedPreferencesBas constructor(
        ctx: Context,
        name: String,
        version: Long,
        mode: Int,
        onUpdate: OnSharedPrefUpgradeListener?
) : SharedPreferences by ctx.getSharedPreferences(name, mode) {
    init {
        //检测版本更新
        val oldVersion: Long = getLong(SHARED_PREF_VERSION_CODE, 0)
        if (version > oldVersion) {
            //写入新的版本号
            edit().putLong(SHARED_PREF_VERSION_CODE, version).apply()
            onUpdate?.onSharedPrefUpgrade(this, oldVersion, version)
        }
    }
}



/**
 * SharedPreferences 属性:本地变量与SharedPreferences之间同步修改
 */
internal class SharedPreferenceProperty<T> private constructor(
    private val key: String,
    private val defaultValue: T
) : ReadWriteProperty<Any?, T> {

    private var _pref: SharedPreferences? = null
    private var _prefProvider: SharedPreferencesProvider? = null

    /**
     * 用于SharedPreferences 动态变换的情况。 i.e. 用户缓存数据
     * @param provider 动态获取SharedPreferences接口
     * @param key
     * @param defaultValue 默认值
     */
    constructor(provider: SharedPreferencesProvider, key: String, defaultValue: T) : this(
        key,
        defaultValue
    ) {
        _prefProvider = provider
    }

    /**
     * 用于存储的SharedPreferences不改变的情况。 i.e. 公共缓存
     * @param defaultValue 默认值
     */
    constructor(sp: SharedPreferences, key: String, defaultValue: T) : this(key, defaultValue) {
        _pref = sp
    }

    /**
     * 获取有效的Pref
     */
    private val priorityPref get() = _prefProvider?.pref ?: _pref

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val prefs = priorityPref ?: return defaultValue
        return findPreference(prefs, key, defaultValue)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        priorityPref?.let {
            putPreference(it, key, value)
        }
    }

    //获取值
    private fun findPreference(prefs: SharedPreferences, key: String, default: T): T {
        return prefs.get(key, default)
    }

    //存放值
    private fun putPreference(sp: SharedPreferences, key: String, value: T) {
        sp.put(key, value)
    }
}