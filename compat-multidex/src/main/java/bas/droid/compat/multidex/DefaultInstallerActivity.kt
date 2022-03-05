package bas.droid.compat.multidex

import android.app.Activity
import android.os.Bundle
import androidx.multidex.MultiDex
/**
 * Created by Lucio on 2021/11/17.
 */
class DefaultInstallerActivity : Activity() {

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
        logi("MultiDex.install 开始: ")
        val startTime = System.currentTimeMillis()
        MultiDex.install(this)
        logi("MultiDex.install 结束，耗时: " + (System.currentTimeMillis() - startTime))
        MultiDexCompat.releaseInstallLock(this)
        finish()
        logi("结束安装进程")
        android.os.Process.killProcess(android.os.Process.myPid())
    }

}