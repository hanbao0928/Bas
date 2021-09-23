@file:JvmName("BasCore")
package com.bas.core.android

import android.app.Application
import android.content.Context

/**
 * Created by Lucio on 2021/9/16.
 */

lateinit var basCtx: Context
private var isBasCoreInit: Boolean = false

/**
 * 初始化Core Lib
 * @param debuggable是否是调试模式
 */
@Synchronized
fun initBasCore(app: Application) {

    if (isBasCoreInit)
        return
    isBasCoreInit = true
    basCtx = app
}