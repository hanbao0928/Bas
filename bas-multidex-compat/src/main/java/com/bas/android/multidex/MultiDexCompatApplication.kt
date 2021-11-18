package com.bas.android.multidex

import android.app.Application
import android.content.Context

/**
 * Created by Lucio on 2021/11/15.
 */
abstract class MultiDexCompatApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDexCompat.install(base)
//        if (BoostMultiDex.isOptimizeProcess(Utility.getCurProcessName(base))) {
//
//        }
    }

}