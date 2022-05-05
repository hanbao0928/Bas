@file:JvmName("BasConfigsKt")
@file:JvmMultifileClass

//notice 不要更改包名
package bas.lib.core

import bas.lib.core.exception.ExceptionHandler
import bas.lib.core.exception.ExceptionMessageTransformer
import bas.lib.core.exception.defaultExceptionHandler
import bas.lib.core.exception.defaultExceptionMessageTransformer

/**
 * 设置异常消息处理器
 */
var exceptionHandler: ExceptionHandler = defaultExceptionHandler

/**
 * 异常消息转换器
 */
var exceptionMessageTransformer: ExceptionMessageTransformer =
    defaultExceptionMessageTransformer
