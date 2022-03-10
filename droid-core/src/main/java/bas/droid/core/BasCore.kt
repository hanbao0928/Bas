/**
 * Created by Lucio on 2021/9/16.
 */
@file:JvmName("BasCore")
@file:JvmMultifileClass

package bas.droid.core

import android.app.Application
import android.content.Context
import android.util.Base64
import bas.lib.core.BasConfigurator
import bas.droid.core.app.BasApplicationManager
import bas.droid.core.app.internal.ApplicationManagerImpl
import bas.droid.core.io.Storage
import bas.droid.core.io.storage
import bas.droid.core.net.URLCoderDroid
import bas.droid.core.util.Base64CoderDroid
import bas.droid.systemui.SystemUi
import bas.droid.systemui.systemUiHandler

lateinit var ctxBas: Context

/**
 * 是否开启调试模式
 */
var isDebuggable: Boolean = BuildConfig.DEBUG

/**
 * app管理器
 */
val appManagerBas: BasApplicationManager = ApplicationManagerImpl

/**
 * 设置自定义[SystemUi]
 */
fun setCustomSystemUiHandler(handler: SystemUi) {
    systemUiHandler = handler
}

fun setStorage(custom: Storage){
    storage = custom
}

/**
 * 初始化Core Lib
 * @param debuggable是否是调试模式
 */
@Synchronized
fun initBasCore(app: Application) {
    if (isBasCoreInit)
        return
    isBasCoreInit = true
    ctxBas = app

    //设置URL编码
    BasConfigurator.setURLCoder(URLCoderDroid)

    //设置base64编解码
    val base64Coder = Base64CoderDroid(Base64.NO_WRAP)
    BasConfigurator.setBase64Encoder(base64Coder)
    BasConfigurator.setBase64Decoder(base64Coder)

    ApplicationManagerImpl.init(app)
}