package com.bas.core.android.util

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import com.bas.core.android.R

/**
 * Created by Lucio on 2021/10/8.
 */

private fun View.startAnim(@AnimRes id: Int) {
    val anim = this.getTag(id) as? Animation ?: AnimationUtils.loadAnimation(context, id)
    clearAnimation()
    startAnimation(anim)
}

fun View.shakeX() {
    startAnim(R.anim.bas_shake_x_anim)
}

fun View.shakeY() {
    startAnim(R.anim.bas_shake_y_anim)
}