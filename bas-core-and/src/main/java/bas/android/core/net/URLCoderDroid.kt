package bas.android.core.net

import android.net.Uri
import bas.lang.Coder
import bas.lang.annotation.Note

/**
 * Created by Lucio on 2022/2/13.
 */
object URLCoderDroid : Coder.URLCoder {

    @Note(
        "在url编码中，我门通常使用的java.net.URLEncoder.encode方法进行url编码，此方法存在一个问题就是会将空格转换成＋，导致iOS那边无法将＋解码成空格" +
                "详细请见：https://www.jianshu.com/p/4a7eb969235d"
    )
    override fun encode(value: String): String {
        return Uri.encode(value)
    }

    override fun decode(value: String): String {
        return Uri.decode(value)
    }

}