package bas.droid.arch


import android.app.Activity
import android.app.Application
import bas.droid.adapter.imageloader.ImageLoaderAdapter
import bas.droid.core.app.AppManager
import bas.droid.core.app.ApplicationManager
import bas.droid.core.initDroidCore
import bas.droid.core.os.processName
import bas.droid.core.util.Logger

/**
 * Created by Lucio on 2020-11-01.
 */
abstract class AppArch : Application(), ApplicationManager by AppManager {

    final override fun onCreate() {
        super.onCreate()
        setupBasDependencies()
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

    /**
     * 主进程执行[Application.onCreate]
     */
    protected open fun onCreateMainProcess(processName: String) {
        setupImageLoader()
    }

    /**
     * 子进程执行[Application.onCreate]
     */
    protected abstract fun onCreateOtherProcess(processName: String)

    /**
     * 初始化Bas依赖库
     */
    protected open fun setupBasDependencies() {
        initDroidCore(this)
        initDroidArch(this)
    }

    /**
     * 初始化ImageLoader
     */
    protected open fun setupImageLoader() {
        ImageLoaderAdapter.init(this)
        val config = ImageLoaderAdapter.Config.Builder()
            .setDiskCacheEnabled(true)
            .setDiskCacheFolderName(ImageLoaderAdapter.DEFAULT_DISK_CACHE_FOLDER_NAME)
            .setMemoryCacheEnabled(true)
            .build()
        ImageLoaderAdapter.setConfig(config)
    }

    private fun log(msg: String) {
        Logger.i("AppArch", msg)
    }


    fun finishAllActivity() {
        activityStack.finishAll()
    }

    val topActivity: Activity? get() = activityStack.getCurrent()
}