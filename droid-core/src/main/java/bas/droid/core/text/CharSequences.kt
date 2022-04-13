/**
 * Created by Lucio on 2021/12/5.
 */

package bas.droid.core.text

import android.text.TextUtils
import bas.lib.core.lang.StringUtils


/**
 *
 * 模糊显示电话号码
 */
fun blurryPhone(phone: String): String {
    return phone.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
}


/**
 * 模糊显示邮箱
 *
 * @return
 */
fun blurryEmail(email: String): String {
    return email.replace("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)".toRegex(), "$1****$3$4")
}


/**
 * Returns whether the given [CharSequence] contains only digits.
 *
 * @see TextUtils.isDigitsOnly
 */
inline fun CharSequence.isDigitsOnly() = StringUtils.isDigitsOnly(this)




