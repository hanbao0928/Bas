package bas.droid.arch.ui

import bas.lib.core.exception.ExceptionMessageTransformer
import bas.lib.core.lang.orDefaultIfNullOrEmpty
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.ExecutionException


class DroidExceptionMessageTransformer : ExceptionMessageTransformer {

    override fun getTransformedMessage(e: Throwable): String {
        when (e) {
            is ConnectException -> {
                return "连接失败，请检查网络。"
            }
            is UnknownHostException -> {
                return "无法连接服务器（请检查网络或服务器）。"
            }
            is SocketTimeoutException -> {
                return "网络连接超时。"
            }
            is HttpException -> {
                return "请求失败：code=${e.code()} message=${e.message()}。"
            }
            is NullPointerException -> {
                return "空异常。"
            }
            is ExecutionException -> {
                if (e.cause?.javaClass == UnknownHostException::class.java) {
                    return "当前网络或服务器异常。"
                } else {
                    return e.cause?.message.orDefaultIfNullOrEmpty(e.message ?: e.toString())
                }
            }
            else -> {
                return e.message.orEmpty()
            }
        }
    }
}

