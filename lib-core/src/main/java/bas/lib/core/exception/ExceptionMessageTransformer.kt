package bas.lib.core.exception

import bas.lib.core.BasConfigurator

/**
 * 设置异常消息转换类
 */
fun BasConfigurator.setExceptionMessageTransformer(transformer: ExceptionMessageTransformer) {
    exceptionMessageTransformer = transformer
}

val defaultExceptionMessageTransformer = object : ExceptionMessageTransformer {
    override fun getTransformedMessage(e: Throwable): String {
        return e.message.orEmpty()
    }
}

var exceptionMessageTransformer: ExceptionMessageTransformer = defaultExceptionMessageTransformer
    private set

interface ExceptionMessageTransformer {
    fun getTransformedMessage(e: Throwable): String
}



