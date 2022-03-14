package bas.droid.core.anim

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.annotation.IdRes

/**
 * Created by Lucio on 2021/10/8.
 */

internal fun View.startAnim(@AnimRes id: Int) {
    val anim = this.getTag(id) as? Animation ?: AnimationUtils.loadAnimation(context, id)
    clearAnimation()
    startAnimation(anim)
}


internal inline fun View.startAnim(@IdRes animTag: Int, initializer: () -> Animation) {
    var anim = this.getTag(animTag) as? Animation
    if (anim == null) {
        anim = initializer()
        this.setTag(animTag, anim)
    }
    clearAnimation()
    startAnimation(anim)
}
