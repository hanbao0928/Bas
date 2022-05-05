package bas.droid.compat.multidex

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.multidex.MultiDex
import java.io.File

/**
 * 官方安装方式
 */
class JitPackInstaller : Installer() {

    private val logger = Logger(TAG)

    override fun install(context: Context) {
        //系统支持MultiDex，直接执行MultiDex.install并返回
        if (IS_VM_MULTIDEX_CAPABLE) {
            val time = measureTimeMillis {
                logger.i("VM has multidex support, invoke MultiDex.install directly.")
                MultiDex.install(context)
            }
            logger.d("MultiDex install cost time:$time")
            return
        }

        //系统不支持
        if (Build.VERSION.SDK_INT < MIN_SDK_VERSION) {
            throw RuntimeException(
                "MultiDex installation failed. SDK ${Build.VERSION.SDK_INT} is unsupported. Min SDK version is $MIN_SDK_VERSION"
            )
        }

        try {
            val applicationInfo = getApplicationInfo(context)
            //避免是test运行环境
            if (applicationInfo == null) {
                logger.w("No ApplicationInfo available, i.e. running on a test Context:MultiDex support library is disabled.")
                return
            }

            logger.d(
                "sourceDir=${applicationInfo.sourceDir}\n" +
                        "dataDir=${applicationInfo.dataDir}"
            )
            doInstallation(context)
        } catch (e: Exception) {
            logger.e("MultiDex installation failure", e)
            throw RuntimeException("MultiDex installation failed (${e.message}).")
        }
    }

    private fun doInstallation(context: Context) {

        val processName = getProcessName(context)

        //安装进程
        if (isInstallerProcess(processName)) {
            logger.i("is multidex install process, ignore:the MultiDex.install method will be call by JitPackInstallActivity.")
            return
        }
        val startTime = System.currentTimeMillis()
        val isMainProcess = processName == context.packageName

        //当前主进程，启动子进程进行安装
        if (isMainProcess) {
            logger.i("is main process,prepare install.")
            //创建文件锁
            createInstallLockFlag(context)
            //启动安装进程
            startInstallProcess(context)
        }

        //主进程或非安装进程，需要等待安装结束
        logger.i("$processName waitUntilDexInstallComplete")
        waitUntilDexInstallComplete(context, processName.orEmpty())

        if (isMainProcess) {
            logger.i(
                "MultiDex install done. \nit takes ${System.currentTimeMillis() - startTime} milliseconds."
            )
        }
    }

    /**
     * 是否是安装进程
     */
    private fun isInstallerProcess(processName: String?): Boolean {
        return processName != null && processName.endsWith(":multidex_compat")
    }

    /*启动安装进程*/
    private fun startInstallProcess(context: Context) {
        val intent = Intent(context, JitPackInstallActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }


    /**
     * 创建安装进程锁标记：通过创建一个文件作为标示
     * 必须是跨进程
     */
    private fun createInstallLockFlag(context: Context) {
        try {
            logger.i("prepare creating process lock file .")
            val file = ProcessFileLock(context)

            val parent = file.parentFile
            if (parent != null && !parent.exists()) {
                parent.mkdirs()
            }
            if (!file.exists()) {
                logger.d("create lock file ${if (file.createNewFile()) "success" else "failure"}.")
            } else {
                logger.d("lock file is exists.")
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            logger.e("create lock file failure:${e.message}", e)
        }
    }

    /**
     * 等待MultiDex安装完成（通过检查Lock file是否存在）
     * @param context
     * @param waitIntervalsMillisecond 睡眠间隔时间
     * @param timeoutMillisecond 超时时间 默认1分钟
     * @return
     */
    private fun waitUntilDexInstallComplete(
        context: Context,
        processName: String,
        waitIntervalsMillisecond: Long = 100,
        timeoutMillisecond: Long = 60 * 1000
    ) {
        val file = ProcessFileLock(context)
        var checkCount = 0
        try {
            logger.d("$processName waitUntilDexInstallComplete: >>>")
            while (file.exists()) {
                Thread.sleep(waitIntervalsMillisecond)
                ++checkCount
                logger.d("$processName waitUntilDexInstallComplete: sleep count = $checkCount")

                val waitTime = waitIntervalsMillisecond * checkCount
                if (waitTime >= timeoutMillisecond) {
                    logger.w("$processName waitUntilDexInstallComplete: timeout(${waitTime})")
                    break
                }
            }
            logger.d("$processName waitUntilDexInstallComplete:finish waiting. takes time = ${waitIntervalsMillisecond * checkCount}")
        } catch (e: Exception) {
            e.printStackTrace()
            logger.e("$processName waitUntilDexInstallComplete: 执行错误 ${e.message}", e)
        }
    }

    companion object {

        internal const val TAG = "JitPackMultiDex"

        /**
         * 进程文件锁
         */
        @JvmStatic
        private fun ProcessFileLock(context: Context): File {
            return File(context.cacheDir.absolutePath, "multi_dex_installer.lock")
        }

        /**
         * 释放安装锁
         */
        @JvmStatic
        internal fun releaseInstallLock(context: Context) {
            val file = ProcessFileLock(context)
            if (file.exists())
                file.delete()
        }
    }


}