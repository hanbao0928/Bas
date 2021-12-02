package bas.leanback.effect

import android.view.View
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation

/**
 * Created by Lucio on 2021/11/30.
 */

/**
 * Shake动画持续时间
 */
var SHAKE_DURATION = 500L

private const val SHAKE_X_ANIM_OFFSET = 0x11
private const val SHAKE_Y_ANIM_OFFSET = 0x12

fun View.shakeX() {
    startAnim(this.hashCode() + SHAKE_X_ANIM_OFFSET) {
        ShakeXAnimation()
    }
}

fun View.shakeY() {
    startAnim(this.hashCode() + SHAKE_Y_ANIM_OFFSET) {
        ShakeYAnimation()
    }
}

/**
 * @param cycles 循环次数，默认3次
 */
fun ShakeAnimation(cycles: Float = 3f): Animation {
    return TranslateAnimation(0.0f, 4.0f, 1.0f, 1.0f)
        .also {
            it.duration = SHAKE_DURATION
            it.interpolator = CycleInterpolator(cycles)
        }

}

fun ShakeXAnimation(cycles: Float = 3f): Animation {
    return TranslateAnimation(0.0f, 4.0f, 0.0f, 0.0f)
        .also {
            it.duration = SHAKE_DURATION
            it.interpolator = CycleInterpolator(cycles)
        }

}

fun ShakeYAnimation(cycles: Float = 3f): Animation {
    return TranslateAnimation(0.0f, 0.0f, 0.0f, 4.0f)
        .also {
            it.duration = SHAKE_DURATION
            it.interpolator = CycleInterpolator(cycles)
        }

}