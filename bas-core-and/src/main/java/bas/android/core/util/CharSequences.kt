package bas.android.core.util

import android.webkit.MimeTypeMap
import com.bas.core.lang.getExtension

/**
 * Created by Lucio on 2021/12/5.
 */


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