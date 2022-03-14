package bas.leanback.layout

import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import bas.droid.core.anim.shakeX
import bas.droid.core.anim.shakeY
import bas.leanback.core.MemoryHelper
import java.util.*

/**
 * Created by Lucio on 2021/11/30.
 * 布局帮助类，处理边界动画、焦点记忆、bringToFront等
 */
class LeanbackLayoutHelper private constructor(
    val layout: ViewGroup,
    val callback: Callback,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) {

    private val memoryHelper: MemoryHelper = MemoryHelper.create(layout, attrs, defStyleAttr)

    private val bringToFrontHelper: BringToFrontHelper = BringToFrontHelper.create(
        layout,
        callback,
        attrs,
        defStyleAttr
    )

    private val borderAnimEnabled: Boolean

    init {
        val ta = layout.context.obtainStyledAttributes(attrs, R.styleable.LeanbackLayout)
        borderAnimEnabled = ta.getBoolean(
            R.styleable.LeanbackLayout_lb_borderAnimEnable,
            DEFAULT_BORDER_ANIM_ENABLED
        )
        ta.recycle()
    }

    @bas.lib.core.lang.annotation.CallByOwner
    fun bringChildToFront(child: View?) {
        bringToFrontHelper.bringChildToFront(child)
    }

    @bas.lib.core.lang.annotation.CallByOwner
    fun getChildDrawingOrder(childCount: Int, drawingPosition: Int): Int {
        return bringToFrontHelper.getChildDrawingOrder(childCount, drawingPosition)
    }

    @bas.lib.core.lang.annotation.CallByOwner
    fun requestChildFocus(child: View?, focused: View?) {
        bringToFrontHelper.requestChildFocus(child, focused)
        memoryHelper.requestChildFocus(child, focused)
    }

    @bas.lib.core.lang.annotation.CallByOwner
    fun addFocusables(views: ArrayList<View>?, direction: Int, focusableMode: Int): Boolean {
        return memoryHelper.addFocusables(views, direction, focusableMode)
    }

    @bas.lib.core.lang.annotation.CallByOwner
    fun onRequestFocusInDescendants(
        direction: Int,
        previouslyFocusedRect: Rect?
    ): Boolean {
        return memoryHelper.onRequestFocusInDescendants(direction, previouslyFocusedRect)
    }

    @bas.lib.core.lang.annotation.CallByOwner
    fun onViewRemoved(child: View?) {
        memoryHelper.onViewRemoved(child)
    }

    @bas.lib.core.lang.annotation.CallByOwner
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

    interface Callback : BringToFrontHelper.Callback {
    }

    companion object {

        const val DEFAULT_BORDER_ANIM_ENABLED = true

        @JvmStatic
        fun create(
            layout: ViewGroup,
            callback: Callback,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0
        ): LeanbackLayoutHelper {
            return LeanbackLayoutHelper(layout, callback, attrs, defStyleAttr)
        }

    }
}
