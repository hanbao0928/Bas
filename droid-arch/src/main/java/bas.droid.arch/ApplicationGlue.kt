package bas.droid.arch

import android.app.Application
import android.util.Log
import bas.droid.core.app.AppManager
import bas.droid.core.app.ApplicationManager
import bas.droid.core.os.processName
import bas.droid.core.util.Logger

class ApplicationGlue(val app: Application, val callback: Callback) :
    ApplicationManager by AppManager {

    /**
     * [Application.onCreate]方法中调用该方法
     */
    fun onCreate() {
        val processName = app.processName
        val packageName = app.packageName
        log("processName = $processName  packageName=$packageName")
        if (processName == app.packageName) {
            log("invoke onCreateMainProcess")
            callback.onCreateMainProcess(processName)
        } else {
            log("invoke onCreateOtherProcess")
            callback.onCreateOtherProcess(processName)
        }
    }

    private fun log(msg: String) {
        Log.i("ApplicationGlue", msg)
    }

    interface Callback {

        fun onCreateMainProcess(processName: String)

        fun onCreateOtherProcess(processName: String)

    }
}