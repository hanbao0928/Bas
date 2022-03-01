/**
 * Created by Lucio on 2021/9/16.
 */
@file:JvmName("BasCore")
@file:JvmMultifileClass

package bas.android.core

import android.app.Application
import android.content.Context
import android.util.Base64
import bas.BasConfigurator
import bas.android.core.app.BasApplicationManager
import bas.android.core.app.internal.ApplicationManagerImpl
import bas.android.core.net.URLCoderDroid
import bas.android.core.util.Base64CoderDroid

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

    //设置URL编码
    BasConfigurator.setURLCoder(URLCoderDroid)

    //设置base64编解码
    val base64Coder = Base64CoderDroid(Base64.NO_WRAP)
    BasConfigurator.setBase64Encoder(base64Coder)
    BasConfigurator.setBase64Decoder(base64Coder)

    ApplicationManagerImpl.init(app)
}