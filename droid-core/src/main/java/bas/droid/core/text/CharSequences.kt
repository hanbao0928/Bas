/**
 * Created by Lucio on 2021/12/5.
 */

package bas.droid.core.text

import android.text.TextUtils
import android.webkit.MimeTypeMap
import bas.lib.core.lang.StringUtils
import bas.lib.core.lang.getExtension

/**
 * Returns whether the given [CharSequence] contains only digits.
 *
 * @see TextUtils.isDigitsOnly
 */
inline fun CharSequence.isDigitsOnly() = StringUtils.isDigitsOnly(this)


/**
 * 获取mime type
 */
@JvmOverloads
fun String.getMimeType(defValue: String = "file/*"): String {
    val suffix = this.getExtension()
    if (suffix.isEmpty())
        return defValue
    val type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix)
    if (!type.isNullOrEmpty()) {
        return type
    }
    return defValue
}


