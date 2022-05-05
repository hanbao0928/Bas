@file:JvmName("BasConfigsKt")
@file:JvmMultifileClass

package bas.lib.core

import bas.lib.core.converter.Converters
import bas.lib.core.converter.JsonConverter
import bas.lib.core.lang.Coder

/**
 * 是否开启调试模式
 */
var debuggable: Boolean = false

/**
 * 序列化和反序列化工具
 */
var jsonConverter: JsonConverter
    get() = Converters.getJsonConverter()
    set(value) = Converters.setJsonConverter(value)

/**
 * URL编解码工具
 */
var urlCoder: Coder.URLCoder
    get() = Coder.getURLCoder()
    set(value) = Coder.setURLCoder(value)

/**
 * Base64编码器
 */
var base64Encoder: Coder.Base64Encoder
    get() = Coder.getBase64Encoder()
    set(value) = Coder.setBase64Encoder(value)

/**
 * Base64解码器
 */
var base64Decoder: Coder.Base64Decoder
    get() = Coder.getBase64Decoder()
    set(value) = Coder.setBase64Decoder(value)

/**
 * 调试执行代码
 */
inline fun <T> T.runOnDebug(action: () -> Unit): T {
    if (debuggable) {
        action()
    }
    return this
}

