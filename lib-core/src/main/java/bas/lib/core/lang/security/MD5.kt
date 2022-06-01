/**
 * Created by Lucio on 2021/11/10.
 */
@file:JvmName("MD5Kt")

package bas.lib.core.lang.security


/**
 * MD5加密，与方法[toMD5Lowercase]相似，算法不同
 * @return 加密后字符串（32位小写字母+数字）
 */
@Deprecated(message = "use md5()", replaceWith = ReplaceWith(expression = "this.md5()", imports = ["bas.lib.core.lang.security.md5"]))
inline fun String?.toMD5(): String {
    if (this == null)
        return ""
    return MD5Utils.MD5(this)
}

/**
 * MD5加密，与方法[toMD5Lowercase]相似，算法不同
 * @return 加密后字符串（32位小写字母+数字）
 */
inline fun String?.md5(): String {
    if (this == null)
        return ""
    return MD5Utils.MD5(this)
}

inline fun String?.toMD5Lowercase(salt: String): String {
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