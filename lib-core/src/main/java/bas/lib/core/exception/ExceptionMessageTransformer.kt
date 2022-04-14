package bas.lib.core.exception


interface ExceptionMessageTransformer {
    fun getTransformedMessage(e: Throwable): String
}

//默认异常消息转换处理器
internal val defaultExceptionMessageTransformer = object : ExceptionMessageTransformer {
    override fun getTransformedMessage(e: Throwable): String {
        return e.message.orEmpty()
    }
}







