package bas.lang.security

import bas.lang.security.MD5Utils

/**
 * Created by Lucio on 2021/11/10.
 */

/**
 *  转换成MD5字符串（标准MD5加密算法）
 */
//fun String?.toMd5(): String {
//    if (this.isNullOrEmpty())
//        return ""
//    var bytes = this.toByteArray(Charsets.UTF_8)
//    val md5 = MessageDigest.getInstance("MD5")
//    bytes = md5.digest(bytes)
//
//    val result = StringBuilder()
//    for (item in bytes) {
//        val hexStr = Integer.toHexString(0xFF and item.toInt())
//        if (hexStr.length < 2) {
//            result.append("0")
//        }
//        result.append(hexStr)
//    }
//    return result.toString()
//}

inline fun String?.toMD5(): String {
    if (this == null)
        return ""
    return MD5Utils.MD5(this)
}

inline fun String?.toMD5(salt: String): String {
    if (this == null)
        return ""
    return MD5Utils.MD5Lowercase(this, salt)
}

inline fun String?.toMD5Uppercase(): String {
    if (this == null)
        return ""
    return MD5Utils.MD5Uppercase(this)
}

inline fun String?.toMD5Uppercase(salt: String): String {
    if (this == null)
        return ""
    return MD5Utils.MD5Uppercase(this, salt)
}