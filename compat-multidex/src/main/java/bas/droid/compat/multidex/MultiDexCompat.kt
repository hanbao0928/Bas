package bas.droid.compat.multidex

import android.content.Context

/**
 * Created by Lucio on 2021/11/15.
 */
object MultiDexCompat {

    @JvmOverloads
    fun install(context: Context, installer: Installer? = null) {
        (installer ?: BoostMultiDexInstaller()).install(context)
    }
}