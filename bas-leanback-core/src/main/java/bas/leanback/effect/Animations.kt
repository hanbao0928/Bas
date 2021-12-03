package bas.leanback.effect

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import java.util.*

/**
 * Created by Lucio on 2021/11/30.
 */

/**
 * 用于生成tag的偏移量
 */
internal const val ANIM_TAG_OFFSET_BASE = 0x1212

/**
 * 动画默认持续时间
 */
internal const val DEFAULT_ANIM_DURATION = 300

private const val SHAKE_X_ANIM_OFFSET = 0x11
private const val SHAKE_Y_ANIM_OFFSET = 0x12

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
