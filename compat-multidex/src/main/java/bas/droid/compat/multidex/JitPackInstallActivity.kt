package bas.droid.compat.multidex

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.multidex.MultiDex
/**
 * Created by Lucio on 2021/11/17.
 */
class JitPackInstallActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val thread: Thread = object : Thread() {
            override fun run() {
                loadMultiDex()
            }
        }
        thread.name = "multi_dex"
        thread.start()
        setContentView(R.layout.multidex_installer_activity_bas)
    }

//    private fun showLoading() {
//        AlertDialog.Builder(this)
//            .setMessage("系统准备中，请稍后...")
//            .show()
//    }

    private fun loadMultiDex() {
        val time = Installer.measureTimeMillis {
            MultiDex.install(this)
        }
        Log.i(JitPackInstaller.TAG,"MultiDex.install method cost time:$time")
        JitPackInstaller.releaseInstallLock(this)
        finish()
        Log.i(JitPackInstaller.TAG,"exit MultiDex.install process.")
        android.os.Process.killProcess(android.os.Process.myPid())
    }

}