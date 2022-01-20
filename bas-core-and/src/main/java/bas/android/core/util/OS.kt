package bas.android.core.util

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process
import androidx.annotation.RequiresApi
import bas.android.core.logi
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*

/**
 * Created by Lucio on 2021/11/16.
 */

/**
 * 获取支持的ABI
 */
fun getSupportABIs(): String {
    if (Build.VERSION.SDK_INT >= 21) {
        return Build.SUPPORTED_ABIS.joinToString(",")
    } else {
        val abis = getPropertyBySystemProperties("ro.product.cpu.abilist")
        if (abis.isEmpty() || abis == UNKNOWN_PROPERTY) {
            val abi1 = getPropertyBySystemProperties("ro.product.cpu.abi")
            val abi2 = getPropertyBySystemProperties("ro.product.cpu.abi2")
            val abi32 = getPropertyBySystemProperties("ro.product.cpu.abilist32")
            val abi64 = getPropertyBySystemProperties("ro.product.cpu.abilist64")

            logi("abi=${abi1} abi=${abi2} abi32=${abi32} abi64=${abi64}")

            val abiSet = LinkedHashSet<String>()
            if (abi1.isNotEmpty() && abi1 != UNKNOWN_PROPERTY) {
                abiSet.add(abi1)
            }
            if (abi2.isNotEmpty() && abi2 != UNKNOWN_PROPERTY) {
                abiSet.add(abi2)
            }
            if (abi32.isNotEmpty() && abi32 != UNKNOWN_PROPERTY) {
                abiSet.addAll(abi32.split(","))
            }

            if (abi64.isNotEmpty() && abi64 != UNKNOWN_PROPERTY) {
                abiSet.addAll(abi64.split(","))
            }
            return abiSet.joinToString(",")
        } else {
            return abis
        }
    }
}

/**
 * 当前是否是主进程
 */
fun Context.isMainProcess(): Boolean {
    val pkgName = packageName
    return pkgName == this.processName
}

/**
 * 获取当前进程名字
 */
val Context.processName: String
    get() {
        if (Build.VERSION.SDK_INT >= 28) {
            return getProcessNameV28()
        } else {
            val pid = Process.myPid()
            val name = getProcessNameFromFile(pid)
            if (!name.isNullOrEmpty())
                return name
            return getProcessNameFromRunningProcesses(this, pid).orEmpty()
        }

    }


@RequiresApi(28)
fun getProcessNameV28(): String {
    return Application.getProcessName()
}

/**
 * 获取当前进程名字（遍历当前运行的app进程）
 * @param pid 进程id
 */
fun getProcessNameFromRunningProcesses(ctx: Context, pid: Int): String? {
    val am = ctx.activityManager ?: return null
    val i = am.runningAppProcesses.iterator()

    try {
        while (i.hasNext()) {
            val info = i.next()
            if (info?.pid == pid) {
                return info.processName
            }
        }
    } catch (e: Exception) {
        return null
    }
    return null
}

/**
 * 获取进程号对应的进程名(文件读取方式-bugly推荐方式)
 *
 * @param pid 进程号
 * @return 进程名
 */
fun getProcessNameFromFile(pid: Int): String? {
    var reader: BufferedReader? = null
    try {
        reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
        var processName: String = reader.readLine()
        if (processName.isNotEmpty()) {
            processName = processName.trim { it <= ' ' }
        }
        return processName
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
    } finally {
        try {
            reader?.close()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }
    return null
}


/**
 * 判断手机是否安装某个应用
 * @param appPkgName  应用包名
 * @return   true：安装，false：未安装
 */
fun Context.isApplicationInstall(appPkgName: String): Boolean {
    val packageManager = packageManager ?: return false
    val pinfos = packageManager.getInstalledPackages(0)
    if (pinfos.isNullOrEmpty())
        return false
    pinfos.forEach {
        if (it.packageName == appPkgName)
            return true
    }
    return false
}

/**
 * 删除某个应用
 * @Note: 清单文件中增加如下权限 <uses-permission android:name="android.permission.REQUEST_DELETE_PACKAGES" />

 */
fun Context.deleteApplication(appPkgName: String) {
    val uri = Uri.fromParts("package", appPkgName, null)
    val intent = Intent(Intent.ACTION_DELETE, uri)
    startActivity(intent)
}