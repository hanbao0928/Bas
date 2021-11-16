package com.bas.core.android.app


import android.app.Activity
import android.app.Application
import com.bas.core.android.app.internal.logi
import com.bas.core.android.appManagerBas
import com.bas.core.android.initBasCore
import com.bas.core.android.util.processName

/**
 * Created by Lucio on 2020-11-01.
 */
abstract class BasApplication : Application(), BasApplicationManager by appManagerBas {

    override fun onCreate() {
        super.onCreate()
        val processName = this.processName
        val packageName = this.packageName
        logi("processName = $processName  packageName=$packageName")
        if (processName == this.packageName) {
            logi("invoke onCreateMainProcess")
            onCreateMainProcess(processName)
        }else{
            logi("invoke onCreateOtherProcess")
            onCreateOtherProcess(processName)
        }
    }

    protected open fun onCreateMainProcess(processName: String) {
        initBasCore(this)
    }

    protected open fun onCreateOtherProcess(processName: String) {

    }

    fun finishAllActivity() {
        activityStack.finishAll()
    }

    val topActivity: Activity? get() = activityStack.getCurrent()
}