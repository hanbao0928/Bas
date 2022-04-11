/**
 * Created by Lucio on 2021/9/16.
 */
@file:JvmName("DroidCoreKt")

package bas.droid.core

import android.app.Application
import android.content.Context
import bas.droid.core.app.internal.ApplicationManagerImpl
import bas.droid.core.io.Storage
import bas.droid.core.io.storage
import bas.droid.core.net.URLCoderDroid
import bas.droid.systemui.SystemUi
import bas.droid.systemui.systemUiHandler
import bas.lib.core.BasConfigurator

lateinit var ctxBas: Application

val appInstance: Application
    get() = ctxBas

/**
 * 是否开启调试模式
 */
var debuggable: Boolean = BuildConfig.DEBUG

internal var isInit: Boolean = false

/**
 * 初始化Core Lib
 * @param debuggable是否是调试模式
 */
@Synchronized
fun initDroidCore(app: Application) {
    if (isInit)
        return
    isInit = true
    ctxBas = app

    //设置URL编码
    BasConfigurator.setURLCoder(URLCoderDroid)

    //设置base64编解码
    val base64Coder = bas.droid.core.util.Base64(android.util.Base64.NO_WRAP)
    BasConfigurator.setBase64Encoder(base64Coder)
    BasConfigurator.setBase64Decoder(base64Coder)

    ApplicationManagerImpl.init(app)
}

/**
 * 调试执行代码
 */
inline fun <T> T.runOnDebug(action: () -> Unit): T {
    if (debuggable) {
        action()
    }
    return this
}

/**
 * 设置自定义[SystemUi]
 */
fun setCustomSystemUiHandler(handler: SystemUi) {
    systemUiHandler = handler
}

/**
 * 设置存储类
 */
fun setStorage(custom: Storage) {
    storage = custom
}

