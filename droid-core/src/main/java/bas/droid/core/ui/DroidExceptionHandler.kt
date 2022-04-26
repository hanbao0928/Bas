package bas.droid.core.ui

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import bas.droid.core.runOnDebug
import bas.lib.core.exception.CommonExceptionHandler
import bas.lib.core.exception.ExceptionHandler
import bas.lib.core.exceptionHandler
import bas.lib.core.exceptionMessageTransformer

/**
 * 扩展友好消息字段，用于将异常转换成对用户比较容易理解的信息。
 */
inline val Throwable.friendlyMessage: String
    get() = exceptionMessageTransformer.getTransformedMessage(
        this
    )

/**
 * 初始化Droid异常帮助类：如果libcore的可用则直接使用droid，否则使用默认的handler
 */
internal fun initDroidExceptionHandler():DroidExceptionHandler{
     return exceptionHandler as? DroidExceptionHandler ?: DefaultDroidExceptionHandler()
}

val droidExceptionHandler: DroidExceptionHandler = initDroidExceptionHandler()

/**
 * 捕获ui异常
 */
inline fun Context.tryUi(action: () -> Unit): Throwable? {
    return try {
        action()
        null
    } catch (e: Throwable) {
        droidExceptionHandler.handleUIException(this, e)
        e
    }
}

inline fun View.tryUi(action: () -> Unit): Throwable? {
    return context.tryUi(action)
}

inline fun Fragment.tryUi(action: () -> Unit): Throwable? {
    return requireActivity().tryUi(action)
}


interface DroidExceptionHandler : ExceptionHandler {

    fun handleUIException(context: Context, e: Throwable)
}

open class DefaultDroidExceptionHandler : CommonExceptionHandler(), DroidExceptionHandler {

    override fun handleCatchException(e: Throwable) {
        runOnDebug {
            e.printStackTrace()
        }
    }

    override fun handleUIException(context: Context, e: Throwable) {
        dialogUI.showAlertDialog(context, e.friendlyMessage, "确定")
    }
}