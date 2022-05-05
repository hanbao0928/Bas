package bas.leanback.tab

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.FocusFinder
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.viewpager.widget.ViewPager

/**
 * TV和Phone上ViewPager的最大差别在交互行为上：
 * Phone中通过滑动响应ViewPager的翻页
 * TV中默认情况会通过遥控器按键进行翻页，在TV上ViewPager通常与顶部导航栏联用，ViewPager最好不响应按键翻页效果
 *
 *
 * 在TV上运行时，默认禁用按键和触摸事件。
 * 在TV上运行默认禁用按键事件，避免通过按键在ViewPager内部进行翻页行为，ViewPager的翻页最好是通过顶部的选项卡焦点变化时更改。
 *
 *  A viewpager with touch and key event handling disabled by default.
 * Key events handling is disabled by default as with the behaviour of viewpager the fragments can change when DPAD keys are pressed and focus is on the content inside the [ViewPager].
 * This is not desirable for a top navigation bar. The fragments should preferably change only when the focused tab changes.
 *
 * [focusOutEnabled]:该属性用于控制点击遥控器左右键时，[ViewPager]内部child View的焦点是否转移出来，默认不转移出来；
 * 举个例子，比如[ViewPager]的child view是一个Fragment，包含三个控件，分别在页面内的顶部左侧、顶部右侧和页面中间。[ViewPager]的顶部是一个TabLayout导航，那么当左侧按钮获取焦点时点击遥控器右键，犹豫TabLayout离该按钮更近，根据默认的寻焦规则，TabLayout的tab item view会获取焦点。
 * 这就是默认的情况，而通常这种情况我们并不希望是顶部TabLayout获取焦点，而是[ViewPager]内右侧按钮获取焦点，因此设置[focusOutEnabled]为false，可以阻止按左右键时焦点移出[ViewPager]而只会在ViewPager内部转移焦点。
 */
class LeanbackViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewPager(context, attrs) {

    private var touchEnabled = false
    private var keyEventEnabled = false

    //遥控器左右按键响应时，是否允许[ViewPager]内的焦点转移到[ViewPager]之外，默认不允许，
    // 即任何情况，在[ViewPager]内部点击遥控器左右键只在内部转移焦点。
    private var focusOutEnabled = false

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.LeanbackViewPager)
        touchEnabled = ta.getBoolean(R.styleable.LeanbackViewPager_touchEnabled_lbt, false)
        keyEventEnabled = ta.getBoolean(R.styleable.LeanbackViewPager_keyEventEnabled_lbt, false)
        focusOutEnabled = ta.getBoolean(R.styleable.LeanbackViewPager_focusOutEnabled_lbt, false)
        ta.recycle()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return touchEnabled && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return touchEnabled && super.onInterceptTouchEvent(ev)
    }

    /**
     * Find the nearest view in the specified direction that wants to take
     * focus.
     *
     * @param focused The view that currently has focus
     * @param direction One of FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, and
     * FOCUS_RIGHT, or 0 for not applicable.
     */
    override fun focusSearch(focused: View?, direction: Int): View? {
        return if (focusOutEnabled || (direction != FOCUS_LEFT && direction != FOCUS_RIGHT)) {
            super.focusSearch(focused, direction)
        } else {
            FocusFinder.getInstance().findNextFocus(this, focused, direction) ?: null
        }
    }

    /**
     * You can call this function yourself to have the scroll view perform
     * scrolling from a key event, just as if the event had been dispatched to
     * it by the view hierarchy.
     *
     * @param event The key event to execute.
     * @return Return true if the event was handled, else false.
     */
    override fun executeKeyEvent(event: KeyEvent): Boolean {
        return keyEventEnabled && super.executeKeyEvent(event)
    }

    /**
     * Setter for enabling/disabling touch events
     * @param enableTouch
     */
    fun setTouchEnabled(enableTouch: Boolean) {
        touchEnabled = enableTouch
    }

    /**
     * Setter for enabling/disabling key events
     * @param enableKeyEvent
     */
    fun setKeyEventsEnabled(enableKeyEvent: Boolean) {
        keyEventEnabled = enableKeyEvent
    }

    /**
     * 触发遥控器左右按键时，是否允许[ViewPager]内的焦点转移到[ViewPager]之外
     */
    fun setFocusOutEnabled(enableFocusOut: Boolean) {
        focusOutEnabled = enableFocusOut
    }
}