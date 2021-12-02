package bas.leanback.layout

import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import bas.leanback.core.CallByOwner
import bas.leanback.core.R
import bas.leanback.effect.shakeX
import bas.leanback.effect.shakeY

/**
 * Created by Lucio on 2021/11/30.
 */
class LeanbackLayoutHelper private constructor(
    val layout: ViewGroup,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) {

    private val borderAnimEnabled: Boolean

    init {
        val ta = layout.context.obtainStyledAttributes(attrs, R.styleable.LeanbackLayout)
        borderAnimEnabled = ta.getBoolean(
            R.styleable.LeanbackLayout_borderAnim_bas,
            DEFAULT_BORDER_ANIM_ENABLED
        )
        ta.recycle()
    }

    @CallByOwner
    fun dispatchUnhandledMove(focused: View?, direction: Int): Boolean {
        if (!borderAnimEnabled || focused == null)
            return false
        performBorderAnim(focused, direction)
        return true
    }

    private fun performBorderAnim(view: View, direction: Int) {
        when (direction) {
            View.FOCUS_LEFT, View.FOCUS_RIGHT -> {
                view.shakeX()
            }
            View.FOCUS_UP, View.FOCUS_DOWN -> {
                view.shakeY()
            }
            else -> {
                view.shakeX()
            }
        }
    }

    companion object {

        const val DEFAULT_BORDER_ANIM_ENABLED = true

        @JvmStatic
        fun create(
            layout: ViewGroup,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0
        ): LeanbackLayoutHelper {
            return LeanbackLayoutHelper(layout, attrs, defStyleAttr)
        }

    }
}
