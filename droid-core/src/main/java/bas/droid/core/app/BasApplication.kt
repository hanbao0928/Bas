package bas.droid.core.app


import android.app.Activity
import android.app.Application
import bas.droid.core.appManagerBas
import bas.droid.core.logi
import bas.droid.core.os.processName

/**
 * Created by Lucio on 2020-11-01.
 */
abstract class BasApplication : Application(), BasApplicationManager by appManagerBas {

    final override fun onCreate() {
        super.onCreate()
        val processName = this.processName
        val packageName = this.packageName
        logi("processName = $processName  packageName=$packageName")
        if (processName == this.packageName) {
            logi("invoke onCreateMainProcess")
            onCreateMainProcess(processName)
        } else {
            logi("invoke onCreateOtherProcess")
            onCreateOtherProcess(processName)
        }
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