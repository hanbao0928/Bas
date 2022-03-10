@file:JvmName("AMCore")
@file:JvmMultifileClass

/**
 * Created by Lucio on 2020-11-09.
 */
package andme.core

import andme.core.exception.CommonExceptionHandler
import andme.core.exception.ExceptionHandler
import andme.core.support.ui.DefaultDialogHandler
import andme.core.support.ui.DefaultToastHandler
import andme.core.support.ui.DialogUI
import andme.core.support.ui.ToastUI
import andme.integration.media.MediaStore
import android.app.Application
import android.content.Context
import bas.lib.core.lang.orDefault
import halo.android.permission.BuildConfig

@Deprecated(message = "请使用ctxAM替换")
lateinit var mApp: Context

lateinit var ctxAM:Context

private var isCoreInit: Boolean = false

/**
 * 初始化Core Lib
 */
@Synchronized
fun initCore(app: Application) {
    if(isCoreInit)
        return
    isCoreInit = true
    mApp = app
    ctxAM = app
}

/**
 * 是否开启调试模式
 */
var isDebuggable: Boolean = BuildConfig.DEBUG


//媒体相关功能支持器懒加载函数
var mediaStoreCreator: (() -> MediaStore)? = null

/**
 * 媒体相关功能支持器：用于媒体操作的统一管理
 */
var mediaStoreAM: MediaStore
    get() {
        return mMediaStore.orDefault {
            mediaStoreCreator?.invoke()
        }.orDefault {
            val clzName = Class.forName("$INTEGRATION_PKG_NAME.media.MediaStoreImpl")
            clzName.getDeclaredConstructor().newInstance() as MediaStore
        }
    }
    set(value) {
        mMediaStore = value
    }


/**
 * 默认异常处理器
 */
val defaultExceptionHandler = CommonExceptionHandler()

/**
 * 全局异常处理器
 */
var exceptionHandlerAM: ExceptionHandler = defaultExceptionHandler

/**
 * 对话框交互
 */
var dialogHandlerAM: DialogUI = DefaultDialogHandler

/**
 * Toast交互
 */
var toastHandlerAM: ToastUI = DefaultToastHandler

