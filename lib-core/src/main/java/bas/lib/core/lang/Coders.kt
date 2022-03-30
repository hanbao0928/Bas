/**
 * Created by Lucio on 2022/2/23.
 * 编码相关代码：URLCode、Base64
 */
package bas.lib.core.lang

import java.nio.charset.Charset


inline fun String.toUrlEncode(): String {
    return Coder.urlEncode(this)
}

inline fun String.toUrlDecode(): String {
    return Coder.urlDecode(this)
}

inline fun ByteArray.toBase64Encode(): ByteArray {
    return Coder.base64Encode(this)
}

inline fun ByteArray.toBase64EncodeString(): String {
    return Coder.base64EncodeToString(this)
}

inline fun String.toBase64Encode(charset: Charset = Charsets.UTF_8): ByteArray {
    return Coder.base64Encode(this, charset)
}

inline fun String.toBase64EncodeString(charset: Charset = Charsets.UTF_8): String {
    return Coder.base64EncodeToString(this,charset)
}

inline fun String.toBase64Decode(charset: Charset = Charsets.UTF_8): ByteArray {
    return Coder.base64Decode(this, charset)
}

inline fun String.toBase64DecodeString(charset: Charset = Charsets.UTF_8): String {
    return Coder.base64DecodeToString(this,charset)
}

inline fun ByteArray.toBase64Decode(): ByteArray {
    return Coder.base64Decode(this)
}

inline fun ByteArray.toBase64DecodeString(): String {
    return Coder.base64DecodeToString(this)
}