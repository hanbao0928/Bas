package bas.droid.core.res

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.util.DisplayMetrics
import android.util.Log
import java.util.*

/**
 * Created by Lucio on 2021/10/18.
 * [Locale]切换帮助类：用于解决多语言国际化，适配Android 24+
 *
 * android各国语言对应的缩写：https://www.cnblogs.com/Free-Thinker/p/5443657.html
 * Unicode 和国际化支持（官方资料）：https://developer.android.google.cn/guide/topics/resources/internationalization?hl=zh-cn
 * 语言和语言区域解析概览：https://developer.android.google.cn/guide/topics/resources/multilingual-support?hl=zh-cn
 */
object LanguageCompat {

    private const val TAG = "LanguageCompat"
    private const val FILE_NAME = "bas_core_and"
    private const val KEY_LANG = "bas_core_locale_language"
    private const val KEY_COUNTRY = "bas_core_locale_country"

    /**
     * 应用程序创建的时候尝试应用之前设置的Local
     */
    fun onApplicationCreate(ctx: Context) {
        val local = getCachedLocale(ctx) ?: return
        applyLocale(ctx,local)
    }

    /**
     * 必须调用该方法
     * 在[android.app.Application.attachBaseContext]调用该方法
     * 在[androidx.appcompat.app.AppCompatActivity.attachBaseContext]调用该方法
     * sample:
     * override fun attachBaseContext(base: Context) {
            val context = LanguageCompat.attachBaseContext(base)
            super.attachBaseContext(context)
        }
     */
    fun attachBaseContext(base: Context): Context {
        if (Build.VERSION.SDK_INT >= 26) {
            //网上说的是8.0之后才需要如此设置
            val locale = getCachedLocale(base) ?: return base
            val configuration = base.resources.configuration
            configuration.setLocale(locale)
            configuration.setLocales(LocaleList(locale))
            return base.createConfigurationContext(configuration)
        } else {
            return base
        }
    }

    /**
     * 在[android.app.Application.onConfigurationChanged]调用该方法
     * 当"设备系统配置发生变化时"会回调[android.app.Application.onConfigurationChanged]，其中包括系统配置的语言环境发生变化；
     * 如果app支持"跟随系统发生变化，那么在该方法中应该进行处理"
     * [getSystemLocale]:用于获取系统Local
     *
     * 不要在[android.app.Activity.onConfigurationChanged]中调用该方法：Activity中触发该方法的情况很多，并且通常Activity要作出对应变化，要考虑是什么引起的变化，可能会recreate或者重启app
     */
    fun onConfigurationChanged(ctx: Context,newConfig: Configuration) {
        val local = getCachedLocale(ctx) ?: return
        applyLocale(ctx, local)
    }

    /**
     * 应用[Locale]，如果[newLocal]与当前本地使用的[Locale]相同，则不会发生配置变化，只会根据[updateCacheIfApply]更新缓存
     * @param updateCacheIfApply 修改[Locale]后是否更新本地缓存数据
     */
    @JvmOverloads
    fun applyLocale(ctx: Context, newLocal: Locale, updateCacheIfApply: Boolean = true): Boolean {
        //注意：经测试发现，全局切换Locale需要更改Application的配置，如果只是使用Activity作为Context改变其Resources，
        // 只会改变所有Activity的配置而不会改变Application对应的Resources 导致使用ApplicationContext读取的Resource资源不会发生改变
        val applicationCtx = ctx.applicationContext
        try {
            log("applyLocale(newLocal=$newLocal,updateCacheIfApply=${updateCacheIfApply})")
            if (newLocal != getCurrentLocale(applicationCtx)) {
                log("applyLocale: not same with current local,update it.")
                val configuration: Configuration = applicationCtx.resources.configuration
                val sdkInt = Build.VERSION.SDK_INT
                if (sdkInt >= 17) {
                    configuration.setLocale(newLocal)
                    if (Build.VERSION.SDK_INT >= 24) {
                        configuration.setLocales(LocaleList(newLocal))
                    }
                } else {
                    configuration.locale = newLocal
                }
                val displayMetrics: DisplayMetrics = applicationCtx.resources.displayMetrics
                applicationCtx.resources.updateConfiguration(configuration, displayMetrics)
                return true
            } else {
                log("applyLocale: locale is same,ignore.")
                return false
            }
        } finally {
            if (updateCacheIfApply) {
                log("applyLocale: update cache")
                setCacheLocale(applicationCtx, newLocal)
            }

        }
    }

    /**
     * 设置缓存的[Locale]
     */
    fun setCacheLocale(ctx: Context, locale: Locale) {
        getSharedPreference(ctx).edit().also {
            it.putString(KEY_LANG, locale.language)
            it.putString(KEY_COUNTRY, locale.country)
        }.apply()
    }

    /**
     * 获取本地缓存配置对应的[Locale]
     */
    fun getCachedLocale(ctx: Context): Locale? {
        val shared = getSharedPreference(ctx)
        val lang = shared.getString(KEY_LANG, null)
        if (lang.isNullOrEmpty())
            return null
        val country = shared.getString(KEY_COUNTRY, null)
        return if (country == null) {
            Locale(lang)
        } else {
            Locale(lang, country)
        }
    }

    /**
     * 获取当前使用的[Locale]
     */
    fun getCurrentLocale(ctx: Context): Locale {
        return if (Build.VERSION.SDK_INT >= 24) {
            //7.0有多语言设置获取顶部的语言
            ctx.resources.configuration.locales.get(0)
        } else {
            ctx.resources.configuration.locale
        }
    }

    /**
     * 获取系统使用的[Locale]
     * 备注：未测试，来源于网络
     */
    fun getSystemLocale():Locale{
        return if (Build.VERSION.SDK_INT >= 24) {
            //7.0有多语言设置获取顶部的语言
            Resources.getSystem().configuration.locales.get(0)
        } else {
            Resources.getSystem().configuration.locale
        }
    }

    /**
     * 藏语对应的[Locale]
     */
    fun BOLocal(): Locale = Locale("bo", "CN")

    private fun getSharedPreference(ctx: Context): SharedPreferences {
        return ctx.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    }

    private fun log(msg: String) {
        Log.d(TAG, msg)
    }
}






