package bas.droid.core.text

import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils

/**
 * 只能正输入整数
 */
class PositiveNumberInputFilter : InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        if (!TextUtils.isDigitsOnly(source)) {
            //用户现在输入的不是纯数字，无效
            return ""
        }

        if (dest.isEmpty()) {
            val first = source[0].toString()
            if (first == "0")
                return ""//纯输入，第一位不为0，无效
        }

        return null
    }
}