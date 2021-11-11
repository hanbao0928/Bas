package com.bas.core.android.app


import android.app.Activity
import android.app.Application
import android.os.Handler
import com.bas.core.android.appManagerBas
import com.bas.core.android.initBasCore

/**
 * Created by Lucio on 2020-11-01.
 */
abstract class BasApplication : Application(), BasApplicationManager by appManagerBas {

    override fun onCreate() {
        super.onCreate()
        initBasCore(this)
    }

    fun finishAllActivity() {
        activityStack.finishAll()
    }

    val topActivity: Activity? get() = activityStack.getCurrent()
}