package bas.leanback.effect

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes

/**
 * Created by Lucio on 2021/11/30.
 */

internal fun View.startAnim(@AnimRes id: Int) {
    val anim = this.getTag(id) as? Animation ?: AnimationUtils.loadAnimation(context, id)
    clearAnimation()
    startAnimation(anim)
}

internal inline fun View.startAnim(animTag: Int, inInitializer: () -> Animation) {
    var anim = this.getTag(animTag) as? Animation
    if (anim == null) {
        anim = inInitializer()
        this.setTag(animTag, anim)
    }
    clearAnimation()
    startAnimation(anim)
}