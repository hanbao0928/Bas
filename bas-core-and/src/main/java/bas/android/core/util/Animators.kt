package bas.android.core.util

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import bas.android.core.R

/**
 * Created by Lucio on 2021/10/8.
 */

private fun View.startAnim(@AnimRes id: Int) {
    val anim = this.getTag(id) as? Animation ?: AnimationUtils.loadAnimation(context, id)
    clearAnimation()
    startAnimation(anim)
}

fun View.shakeX() {
    startAnim(R.anim.shake_x_anim_bas)
}

fun View.shakeY() {
    startAnim(R.anim.shake_y_anim_bas)
}