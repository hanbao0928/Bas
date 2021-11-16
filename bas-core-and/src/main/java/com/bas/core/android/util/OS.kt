package com.bas.core.android.util

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * Created by Lucio on 2021/11/16.
 */

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
        val pid = Process.myPid()
        val name = getProcessNameFromFile(pid)
        if (!name.isNullOrEmpty())
            return name
        return getProcessNameFromRunningProcesses(this, pid).orEmpty()
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
            val info = i.next() as? ActivityManager.RunningAppProcessInfo
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