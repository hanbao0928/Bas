package com.bas.android.multidex

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import java.io.File
import java.util.*


/**
 * Created by Lucio on 2021/11/15.
 */
object MultiDexCompat {

    private const val VM_WITH_MULTIDEX_VERSION_MAJOR = 2
    private const val VM_WITH_MULTIDEX_VERSION_MINOR = 1

    //VM 是否支持MultiDex
    private val IS_VM_MULTIDEX_CAPABLE = isVMMultidexCapable(System.getProperty("java.vm.version"))
    private const val MIN_SDK_VERSION = 4

    @JvmOverloads
    fun install(context: Context, installer: Installer? = null) {
        val packageName = context.packageName
        val processName = getProcessName(context)
        if (packageName != processName) {
            logi("当前不是主进程（processName=${processName}），不执行MultiDex安装")
            return
        }
        val startTime = System.currentTimeMillis()
        logi("Installing application")
        if (IS_VM_MULTIDEX_CAPABLE) {
            logi("VM has multidex support, MultiDex support library is disabled.")
            return
        }

        if (Build.VERSION.SDK_INT < MIN_SDK_VERSION) {
            throw RuntimeException(
                "MultiDex installation failed. SDK " + Build.VERSION.SDK_INT
                        + " is unsupported. Min SDK version is " + MIN_SDK_VERSION + "."
            )
        }

        try {
            val applicationInfo = getApplicationInfo(context)
            if (applicationInfo == null) {
                logi(
                    "No ApplicationInfo available, i.e. running on a test Context:" +
                            "  MultiDex support library is disabled."
                )
                return
            }

            logd(
                "sourceDir=${applicationInfo.sourceDir}\n" +
                        "dataDir=${applicationInfo.dataDir}"
            )
            doInstallation(context, installer)
        } catch (e: Exception) {
            loge("MultiDex installation failure", e)
            throw RuntimeException("MultiDex installation failed (" + e.message + ").")
        }
        logi(
            "install done。" +
                    "it takes ${System.currentTimeMillis() - startTime} milliseconds。"
        )
    }

    private fun doInstallation(context: Context, custom: Installer?) {
        val installer = custom ?: DefaultMultiDexInstaller()

        //安装过程是异步过程，则需要在后面
        if (!installer.isSyncMode) {
            createInstallLockFlag(context)
        }
        installer.doInstall(context)

        if (!installer.isSyncMode) {
            waitUntilDexInstallComplete(context)
            installer.doSecondInstall(context)
        }
    }

    private fun LockFile(context: Context): File {
        return File(context.cacheDir.absolutePath, "multi_dex_installer.lock")
    }

    /**
     * 创建安装进程锁标记：通过创建一个文件作为标示
     * 必须是跨进程
     */
    fun createInstallLockFlag(context: Context) {
        try {
            logi("lock install process.")
            val file = LockFile(context)
            if (!file.exists()) {
                logd("create lock file.")
                file.createNewFile()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            loge("lock install process fail.", e)
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
        waitIntervalsMillisecond: Long = 100,
        timeoutMillisecond: Long = 60 * 1000
    ) {
        val file = LockFile(context)
        var checkCount = 0
        try {
            logd("waitUntilDexInstallComplete: >>>")
            while (file.exists()) {
                Thread.sleep(waitIntervalsMillisecond)
                ++checkCount
                logd("sleep count = $checkCount")

                val waitTime = waitIntervalsMillisecond * checkCount
                if (waitTime >= timeoutMillisecond) {
                    logw("waitUntilDexInstallComplete: 等待超时，结束等待。等待时间${waitTime}")
                    break
                }
            }
            logd("waitUntilDexInstallComplete:等待结束，等待时间:${waitIntervalsMillisecond * checkCount}")
        } catch (e: Exception) {
            e.printStackTrace()
            loge("waitUntilDexInstallComplete: 执行错误", e)
        }
    }

    /**
     * 释放安装锁
     */
    fun releaseInstallLock(context: Context) {
        val file = LockFile(context)
        if (file.exists())
            file.delete()
    }

    //是否是主进程
    private fun isMainProcess(context: Context): Boolean {
        return context.packageName == getProcessName(context)
    }

    /**
     * 判断当前VM是否支持MultiDex(从MultiDex 拷出来的的方法)
     * @return true if the VM handles multidex
     */
    private fun isVMMultidexCapable(versionString: String?): Boolean {
        var isMultidexCapable = false
        if (versionString != null) {
            val tokenizer = StringTokenizer(versionString, ".")
            val majorToken = if (tokenizer.hasMoreTokens()) tokenizer.nextToken() else null
            val minorToken = if (tokenizer.hasMoreTokens()) tokenizer.nextToken() else null
            if (majorToken != null && minorToken != null) {
                try {
                    val major = majorToken.toInt()
                    val minor = minorToken.toInt()
                    isMultidexCapable = (major > VM_WITH_MULTIDEX_VERSION_MAJOR
                            || (major == VM_WITH_MULTIDEX_VERSION_MAJOR
                            && minor >= VM_WITH_MULTIDEX_VERSION_MINOR))
                } catch (e: NumberFormatException) {
                    // let isMultidexCapable be false
                }
            }
        }
        logi(
            "VM with version " + versionString +
                    if (isMultidexCapable) " has multidex support" else " does not have multidex support"
        )
        return isMultidexCapable
    }

    //获取当前程序信息
    private fun getApplicationInfo(context: Context): ApplicationInfo? {
        return try {
            /* Due to package install races it is possible for a process to be started from an old
                   * apk even though that apk has been replaced. Querying for ApplicationInfo by package
                   * name may return information for the new apk, leading to a runtime with the old main
                   * dex file and new secondary dex files. This leads to various problems like
                   * ClassNotFoundExceptions. Using context.getApplicationInfo() should result in the
                   * process having a consistent view of the world (even if it is of the old world). The
                   * package install races are eventually resolved and old processes are killed.
                   */
            context.applicationInfo
        } catch (e: java.lang.RuntimeException) {
            /* Ignore those exceptions so that we don't break tests relying on Context like
                   * a android.test.mock.MockContext or a android.content.ContextWrapper with a null
                   * base Context.
                   */
            logw(
                "Failure while trying to obtain ApplicationInfo from Context. " +
                        "Must be running in test mode. Skip patching.", e
            )
            null
        }
    }

    //获取当前进程名字
    private fun getProcessName(ctx: Context): String? {
        if (Build.VERSION.SDK_INT >= 28) {
            return Application.getProcessName()
        } else {
            val pid = android.os.Process.myPid()
            val am = ctx
                .getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager ?: return null
            val appProcesses = am.runningAppProcesses.iterator()
            try {
                while (appProcesses.hasNext()) {
                    val info = appProcesses.next()
                    if (info?.pid == pid) {
                        return info.processName
                    }
                }
            } catch (e: Exception) {
                return null
            }
            return null
        }
    }

}