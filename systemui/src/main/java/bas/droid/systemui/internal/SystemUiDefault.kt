package bas.droid.systemui.internal

import bas.droid.systemui.SystemUi
import android.app.Activity

/**
 * Created by Lucio on 2020-11-15.
 */

internal class SystemUiDefault : SystemUi {

    override fun setImmersiveStatusBar(activity: Activity, color1: Int, color2: Int, ratio: Float) {
    }

    override fun setStatusBarColor(activity: Activity, color1: Int, color2: Int, ratio: Float) {
    }

    override fun setImmersiveNavigationBar(activity: Activity, color1: Int, color2: Int, ratio: Float) {
    }

    override fun setNavigationBarColor(activity: Activity, color1: Int, color2: Int, ratio: Float) {
    }

    override fun setImmersiveSystemBar(activity: Activity, color1: Int, color2: Int, ratio: Float) {
    }

    override fun setSystemBarColor(activity: Activity, color1: Int, color2: Int, ratio: Float) {
    }

    override fun setStatusBarLightMode(activity: Activity) {
    }

    override fun setStatusBarDarkMode(activity: Activity) {
    }

}