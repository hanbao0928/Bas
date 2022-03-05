package bas.droid.compat.multidex

import android.app.Application
import android.content.Context

/**
 * Created by Lucio on 2021/11/15.
 */
abstract class MultiDexCompatApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDexCompat.install(base, getMultidexInstaller())
    }

    protected open fun getMultidexInstaller(): Installer?{
        return null
    }

}