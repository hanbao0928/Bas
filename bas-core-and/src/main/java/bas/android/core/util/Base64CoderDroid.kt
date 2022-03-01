package bas.android.core.util

import android.os.Build
import bas.lang.Coder
import java.util.*

/**
 * Created by Lucio on 2022/2/23.
 */
class Base64CoderDroid(private val flag: Int = android.util.Base64.NO_WRAP) : Coder.Base64Encoder,
    Coder.Base64Decoder {

    override fun encode(input: ByteArray): ByteArray {
        return if (Build.VERSION.SDK_INT >= 26) {
            Base64.getEncoder().encode(input)
        } else {
            android.util.Base64.encode(input, flag)
        }
    }

    override fun encodeToString(input: ByteArray): String {
        return String(encode(input))
    }

    override fun decode(input: ByteArray): ByteArray {
        return if (Build.VERSION.SDK_INT >= 26) {
            Base64.getDecoder().decode(input)
        } else {
            android.util.Base64.decode(input, flag)
        }
    }

    override fun decodeToString(input: ByteArray): String {
        return String(decode(input))
    }
}