/**
 * Created by Lucio on 2021/9/16.
 */
@file:JvmName("BasCore")
@file:JvmMultifileClass

package com.bas.core.android

import android.app.Application
import android.content.Context
import com.bas.core.android.app.BasApplicationManager
import com.bas.core.android.app.internal.ApplicationManagerImpl

lateinit var basCtx: Context

/**
 * 是否开启调试模式
 */
var isDebuggable: Boolean = BuildConfig.DEBUG

/**
 * app管理器
 */
val appManagerBas: BasApplicationManager = ApplicationManagerImpl

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
    ApplicationManagerImpl.init(app)
}