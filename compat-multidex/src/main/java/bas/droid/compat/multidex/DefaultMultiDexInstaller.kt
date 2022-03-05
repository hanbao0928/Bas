package bas.droid.compat.multidex

import android.content.Context
import android.content.Intent
import androidx.multidex.MultiDex

/**
 * Created by Lucio on 2021/11/17.
 * 官方安装方式
 */
class DefaultMultiDexInstaller(override var isSyncMode: Boolean = false) : Installer {


    override fun doInstall(context: Context) {
        //启动另一个进程去加载MultiDex
        val intent = Intent(context, DefaultInstallerActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
//
//        //另一个进程以及加载 MultiDex，有缓存了，所以主进程再加载就很快了。
//        //为什么主进程要再加载，因为每个进程都有一个ClassLoader
//        val startTime = System.currentTimeMillis()
//        MultiDex.install(context)
//        logd("第二次 MultiDex.install 结束，耗时: " + (System.currentTimeMillis() - startTime))
//
//        preNewActivity()
    }

    /**
     * 第二次安装
     */
    override fun doSecondInstall(context: Context) {
        MultiDex.install(context)
    }


}