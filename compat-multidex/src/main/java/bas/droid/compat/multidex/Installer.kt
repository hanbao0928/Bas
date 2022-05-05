package bas.droid.compat.multidex

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import java.util.*

abstract class Installer {

    abstract fun install(context: Context)

    companion object {

        private const val VM_WITH_MULTIDEX_VERSION_MAJOR = 2
        private const val VM_WITH_MULTIDEX_VERSION_MINOR = 1

        //VM 是否支持MultiDex
        internal val IS_VM_MULTIDEX_CAPABLE =
            isVMMultidexCapable(System.getProperty("java.vm.version"))

        internal const val MIN_SDK_VERSION = 4

        //获取当前进程名字
        internal fun getProcessName(ctx: Context): String? {
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
            println(
                "VM with version " + versionString +
                        if (isMultidexCapable) " has multidex support" else " does not have multidex support"
            )
            return isMultidexCapable
        }

        //获取当前程序信息
        internal fun getApplicationInfo(context: Context): ApplicationInfo? {
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
            } catch (e: RuntimeException) {
                /* Ignore those exceptions so that we don't break tests relying on Context like
                       * a android.test.mock.MockContext or a android.content.ContextWrapper with a null
                       * base Context.
                       */
                e.printStackTrace()
                println(
                    "Failure while trying to obtain ApplicationInfo from Context. Must be running in test mode. Skip patching."
                )
                null
            }
        }

        internal fun measureTimeMillis(block: () -> Unit): Long {
            val start = System.currentTimeMillis()
            block()
            return System.currentTimeMillis() - start
        }
    }


}