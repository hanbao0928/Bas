package bas.lib.core.exception


/**
 * 异常处理
 */
inline fun <T> T.tryCatch(action: T.() -> Unit): Throwable? {
    return try {
        action()
        null
    } catch (e: Throwable) {
        exceptionHandler.handleCatchException(e)
        e
    }
}

inline fun <T> T.tryIgnore(action: T.() -> Unit): Throwable? {
    return try {
        action(this)
        null
    } catch (e: Throwable) {
        e
    }
}

inline fun <T> Throwable?.onCatch(block: (Throwable) -> T): T? {
    if (this == null)
        return null
    return block(this)
}

/**
 * 异常处理器,用于处理程序中相关的各种类型的异常
 */
interface ExceptionHandler {

    /**
     * 处理未捕获的异常
     */
    fun handleUncaughtException(thread: Thread, e: Throwable)

    /**
     * 处理被捕获的常规异常：即用户通过tryCatch函数捕获的异常
     */
    fun handleCatchException(e: Throwable)

}

open class CommonExceptionHandler : ExceptionHandler {

    override fun handleUncaughtException(thread: Thread, e: Throwable) {
        //未捕获的异常默认将异常信息写入文件中
        //文件存储路径：/Android/data/{packagename}/file/crash/{yyyy-mm-dd}/{yyyy-mm-dd}
        //todo  保存异常信息到本地
    }

    override fun handleCatchException(e: Throwable) {
        e.printStackTrace()
    }
}

internal val defaultExceptionHandler: ExceptionHandler = CommonExceptionHandler()


