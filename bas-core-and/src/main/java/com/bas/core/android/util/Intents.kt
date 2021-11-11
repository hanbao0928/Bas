package com.bas.core.android.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent

/**
 * Created by Lucio on 2021/10/27.
 */

/**
 * 根据包名创建指定启动意图
 */
fun LauncherIntent(ctx: Context, pkgName: String): Intent? {
    return ctx.packageManager.getLaunchIntentForPackage(pkgName)
}


/**
 * 根据包名和组件名创建启动意图
 */
fun LauncherIntent(pkgName: String, clsName: String): Intent {
    return Intent().apply {
        component = ComponentName(pkgName, clsName)
    }
}