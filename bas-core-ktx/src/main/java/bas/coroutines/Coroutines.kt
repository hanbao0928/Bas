package bas.coroutines

import bas.lang.exception.RetryException
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Created by Lucio on 2021/11/10.
 */

/**
 * 在io线程执行
 */
suspend inline fun <T> ioInvoke(
    crossinline block: suspend CoroutineScope.() -> T
): T {
    return withContext(Dispatchers.IO) {
        block()
    }
}

/**
 * 主线程执行
 */
suspend inline fun <T> mainInvoke(
    crossinline block: suspend CoroutineScope.() -> T
): T {
    return withContext(Dispatchers.Main) {
        block()
    }
}

/**
 * 可重试执行
 */
fun CoroutineScope.retryableLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return launch(context, start) {
        var relaunch = true
        while (relaunch) {
            try {
                block.invoke(this)
                relaunch = false
            } catch (e: RetryException) {
                //nothing
            }
        }
    }

}


/**
 * 可重试执行
 */
fun <T> CoroutineScope.retryableAsync(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> {
    return async(context, start) {
        var result: Any? = null
        var reinvoke = true
        while (reinvoke) {
            try {
                result = block.invoke(this)
                reinvoke = false
            } catch (e: RetryException) {
            }
        }
        result as T
    }
}