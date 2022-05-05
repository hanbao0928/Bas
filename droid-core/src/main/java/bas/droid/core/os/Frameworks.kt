@file:JvmName("FrameworksKt")
package bas.droid.core.os

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import bas.droid.core.content.InstallAppIntent
import bas.droid.core.util.activityManager
import halo.android.permission.HaloSpecPermission
import halo.android.permission.spec.SpecialListener
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * Created by Lucio on 2021/11/16.
 */


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
 * 安装app
 */
fun FragmentActivity.installApp(path: String, onPermissionDeny: (() -> Unit)? = null) {
    HaloSpecPermission.requestAppUnknownSource(this, object : SpecialListener {
        override fun onSpecialDeny() {
            onPermissionDeny?.invoke()
        }

        override fun onSpecialGrand() {
            if (this@installApp.isFinishing) {
                Log.w("Droid", "installApp fail. Activity is finishing.")
                return
            }
            val intent = InstallAppIntent(this@installApp, path)
            this@installApp.startActivity(intent)
        }
    })
}

/**
 * 安装app
 */
fun Fragment.installApp(path: String, onPermissionDeny: (() -> Unit)? = null) {
    HaloSpecPermission.requestAppUnknownSource(this, object : SpecialListener {
        override fun onSpecialDeny() {
            onPermissionDeny?.invoke()
        }

        override fun onSpecialGrand() {
            val intent = InstallAppIntent(requireContext(), path)
            requireActivity().startActivity(intent)
        }
    })
}

/**
 * 卸载一个app
 * @param packageName app对应（被删除）的包名
 * @Note: 清单文件中增加如下权限 <uses-permission android:name="android.permission.REQUEST_DELETE_PACKAGES" />
 */
fun Context.uninstallApp(packageName: String) {
    val uri = Uri.fromParts("package", packageName, null)
    val intent = Intent(Intent.ACTION_DELETE, uri)
    startActivity(intent)
}


/**
 * 获取支持的ABI
 * @return 返回所有支持的cpu类型，用","拼接
 */
fun getSupportABIs(): String {
    if (Build.VERSION.SDK_INT >= 21) {
        return Build.SUPPORTED_ABIS.joinToString(",")
    } else {
        val defValue = UNKNOWN_PROPERTY
        val abis = getSystemPropertyByReflect("ro.product.cpu.abilist", defValue)
        if (abis.isEmpty() || abis == defValue) {
            val abi1 = getSystemPropertyByReflect("ro.product.cpu.abi", defValue)
            val abi2 = getSystemPropertyByReflect("ro.product.cpu.abi2", defValue)
            val abi32 = getSystemPropertyByReflect("ro.product.cpu.abilist32", defValue)
            val abi64 = getSystemPropertyByReflect("ro.product.cpu.abilist64", defValue)

            val abiSet = LinkedHashSet<String>()
            if (!abi1.isInvalidPropertyValue(defValue)) {
                abiSet.add(abi1)
            }
            if (!abi2.isInvalidPropertyValue(defValue)) {
                abiSet.add(abi2)
            }
            if (!abi32.isInvalidPropertyValue(defValue)) {
                abiSet.addAll(abi32.split(","))
            }
            if (!abi64.isInvalidPropertyValue(defValue)) {
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
            val name = getProcessNameByFile(pid)
            if (!name.isNullOrEmpty())
                return name
            return getProcessNameByRunningProcesses(this, pid).orEmpty()
        }
    }


@RequiresApi(28)
private fun getProcessNameV28(): String {
    return Application.getProcessName()
}

/**
 * 获取当前进程名字（遍历当前运行的app进程）
 * @param pid 进程id
 */
fun getProcessNameByRunningProcesses(ctx: Context, pid: Int): String? {
    val am = ctx.activityManager ?: return null
    val i = am.runningAppProcesses.iterator()
    try {
        while (i.hasNext()) {
            val info = i.next()
            if (info?.pid == pid) {
                return info.processName
            }
        }
    } catch (e: Throwable) {
    }
    return null
}

/**
 * 获取进程号对应的进程名(文件读取方式-bugly推荐方式)
 *
 * @param pid 进程号
 * @return 进程名
 */
fun getProcessNameByFile(pid: Int): String? {
    var reader: BufferedReader? = null
    try {
        reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
        var processName: String = reader.readLine()
        if (processName.isNotEmpty()) {
            processName = processName.trim { it <= ' ' }
        }
        return processName
    } catch (e: Throwable) {
        e.printStackTrace()
    } finally {
        try {
            reader?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return null
}
