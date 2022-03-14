/**
 * Created by Lucio on 2021/11/30.
 */
package bas.droid.core.anim

import android.view.View
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import bas.droid.core.R


/**
 * Shake动画持续时间
 */
@JvmField
var SHAKE_DURATION = 500L


fun View.shakeX() {
    startAnim(R.id.shake_x_anim_tag_bas) {
        ShakeXAnimation()
    }
}

fun View.shakeY() {
    startAnim(R.id.shake_y_anim_tag_bas) {
        ShakeYAnimation()
    }
}

/**
 * @param cycles 循环次数，默认3次
 */
fun ShakeXAnimation(cycles: Float = 3f): Animation {
    return TranslateAnimation(0.0f, 4.0f, 0.0f, 0.0f)
        .also {
            it.duration = SHAKE_DURATION
            it.interpolator = CycleInterpolator(cycles)
        }

}

/**
 * @param cycles 循环次数，默认3次
 */
fun ShakeYAnimation(cycles: Float = 3f): Animation {
    return TranslateAnimation(0.0f, 0.0f, 0.0f, 4.0f)
        .also {
            it.duration = SHAKE_DURATION
            it.interpolator = CycleInterpolator(cycles)
        }

}


