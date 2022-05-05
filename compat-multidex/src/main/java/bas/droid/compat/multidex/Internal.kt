package bas.droid.compat.multidex

import android.util.Log

/**
 * Created by Lucio on 2021/11/17.
 */

internal class Logger(private val tag: String) {

    var debug = true

    internal fun d(msg: String) {
        if (!debug)
            return
        Log.d(tag, msg)
    }

    internal fun i(msg: String) {
        Log.i(tag, msg)
    }

    @JvmOverloads
    internal fun w(msg: String, e: Throwable? = null) {
        Log.w(tag, msg, e)
    }

    @JvmOverloads
    internal fun e(msg: String, e: Throwable? = null) {
        Log.e(tag, msg, e)
    }
}