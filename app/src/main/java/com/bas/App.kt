package com.bas

import android.content.Context
import android.content.Intent
import android.os.Handler
import com.bas.android.leanback.flexbox.LeanbackFlexboxLayout
import com.bas.android.multidex.MultiDexCompat
import com.bas.core.android.app.BasApplication
import com.bas.core.android.util.Logger
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

/**
 * Created by Lucio on 2021/9/24.
 */
class App : BasApplication() {

    override fun onCreateMainProcess(processName: String) {
    }

    /**
     * 子进程执行[Application.onCreate]
     */
    override fun onCreateOtherProcess(processName: String) {

    }

//    override fun onCreate() {
//        super.onCreate()
//
////        initBasCore(this)
////
////        val strategy = CrashReport.UserStrategy(this)
////        strategy.isUploadProcess = true
////        CrashReport.initCrashReport(this, "644e75b030", BuildConfig.DEBUG, strategy)
////        CaocConfig.Builder.create()
////            .backgroundMode(CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
////            .enabled(true) //default: true
////            .showErrorDetails(true) //default: true
////            .showRestartButton(true) //default: true
////            .logErrorOnRestart(true) //default: true
////            .trackActivities(false) //default: false
////            .minTimeBetweenCrashesMs(2000) //default: 3000
//////                .errorDrawable(R.drawable.ic_custom_drawable) //default: bug image
//////                .restartActivity(YourCustomActivity::class.java) //default: null (your app's launch activity)
//////                .errorActivity(YourCustomErrorActivity::class.java) //default: null (default error activity)
//////                .eventListener(YourCustomEventListener()) //default: null
////            .eventListener(object : CustomActivityOnCrash.EventListener {
////                override fun onRestartAppFromErrorActivity() {
////
////                }
////
////                override fun onCloseAppFromErrorActivity() {
////                }
////
////                override fun onLaunchErrorActivity() {
////                }
////
////            })
////            .apply()
////
////        val map = HashMap<String, Any>()
////        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
////        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
////        QbSdk.initTbsSettings(map)
//    }

    /**
     * Set the base context for this ContextWrapper.  All calls will then be
     * delegated to the base context.  Throws
     * IllegalStateException if a base context has already been set.
     *
     * @param base The new base context for this wrapper.
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDexCompat.install(this)
    }

}