package bas.droid.arch


import android.app.Activity
import android.app.Application
import bas.droid.core.app.AppManager
import bas.droid.core.app.ApplicationManager
import bas.droid.core.initDroidCore
import bas.droid.core.os.processName
import bas.droid.core.util.Logger

/**
 * Created by Lucio on 2020-11-01.
 */
abstract class AppArch : Application(), ApplicationManager by AppManager {

    override fun onCreate() {
        super.onCreate()
        initDroidCore(this)
        val processName = this.processName
        val packageName = this.packageName
        log("processName = $processName  packageName=$packageName")
        if (processName == this.packageName) {
            log("invoke onCreateMainProcess")
            onCreateMainProcess(processName)
        } else {
            log("invoke onCreateOtherProcess")
            onCreateOtherProcess(processName)
        }

    }

    private fun log(msg: String) {
        Logger.i("AppArch", msg)
    }

    /**
     * 主进程执行[Application.onCreate]
     */
    abstract fun onCreateMainProcess(processName: String)

    /**
     * 子进程执行[Application.onCreate]
     */
    abstract fun onCreateOtherProcess(processName: String)

    fun finishAllActivity() {
        activityStack.finishAll()
    }

    val topActivity: Activity? get() = activityStack.getCurrent()
}