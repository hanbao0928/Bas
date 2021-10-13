package com.bas.android.leanback.tab

import android.content.Context
import android.util.AttributeSet
import android.view.FocusFinder
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import java.util.*

/**
 * Created by Lucio on 2021/10/11.
 *
 * TV和Phone使用TabLayout的最大区别在于交互上：
 *  Phone上是通过Tab的点击进行ViewPager的翻页
 *  TV上应该是通过Tab的焦点获取进行ViewPager的翻页
 */
class LeanbackTabLayout : TabLayout {

    private var focusOutEnabled: Boolean = true
    private var focusMemoryEnabled:Boolean = true

    private var viewPager: ViewPager? = null

    /**
     * 是否支持在TV上运行
     * 默认支持在TV上运行，原因如下：
     * 1、在某些电视上运行通过[com.bas.core.android.util.isTVUIMode]读取到的值为false
     * 2、支持TV运行也不影响在手机上的运行
     */
    private var isLeanbackMode: Boolean = true

    /**
     * 与ViewPager联动
     */
    private var mediator: LeanbackTabLayoutMediator? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    /**
     * 设置是否是电视上的操作模式
     * 电视上通过Tab的焦点改变切换ViewPager
     * 手机上通过Tab的点击切换ViewPager
     */
    fun setLeanbackMode(isLeanback: Boolean) {
        this.isLeanbackMode = isLeanback
    }

    /**
     * 横向边界（左或右）是否允许焦点移出
     */
    fun setFocusOutEnabled(enableFocusOut: Boolean) {
        focusOutEnabled = enableFocusOut
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
        //如果允许焦点移出、或者不是点击遥控器左右键、或者当前为非固定Tab模式并且可以滚动（tab的内容不足整个TabLayout）
        if (focusOutEnabled || (direction != FOCUS_LEFT && direction != FOCUS_RIGHT)
            || (tabMode != MODE_FIXED && canScroll())
        ) {
            return super.focusSearch(focused, direction)
        } else {
            return FocusFinder.getInstance().findNextFocus(this, focused, direction) ?: focused
        }
    }

    /**
     * 是否启用焦点记忆：获取焦点时让之前已选中的tab获取焦点
     */
    fun setFocusMemoryEnabled(enableFocusMemory: Boolean){
        this.focusMemoryEnabled = enableFocusMemory
    }

    override fun addFocusables(views: ArrayList<View>?, direction: Int, focusableMode: Int) {
        if(views == null
            || tabCount <= 0
            || direction == FOCUS_LEFT
            || direction == FOCUS_RIGHT
            || !focusMemoryEnabled
            || (canTakeFocus() && this.descendantFocusability == FOCUS_BLOCK_DESCENDANTS)){
            return super.addFocusables(views, direction, focusableMode)
        }

        val selectedTab = getTabAt(selectedTabPosition)?: getTabAt(0) ?: return super.addFocusables(views, direction, focusableMode)
        views.add(selectedTab.view)
    }

    private fun canTakeFocus(): Boolean{
        return isFocusable && isVisible && isEnabled
    }

    /**
     * @return Returns true this HorizontalScrollView can be scrolled
     */
    private fun canScroll(): Boolean {
        val child = getChildAt(0)
        if (child != null) {
            val childWidth = child.width
            return width < childWidth + paddingLeft + paddingRight
        }
        return false
    }

    override fun setupWithViewPager(viewPager: ViewPager?, autoRefresh: Boolean) {
        if (viewPager == null) {
            detachViewPager()
            return
        }
        setupWithViewPager(
            viewPager,
            TabConfigurationStrategy.ViewPagerStrategy(viewPager),
            autoRefresh
        )
    }

    /**
     * 根据ViewPager进行初始化；
     * 调用此方法之前需要ViewPager已设置adapter
     */
    fun setupWithViewPager(
        viewPager: ViewPager?,
        tabConfigurationStrategy: TabConfigurationStrategy,
        autoRefresh: Boolean = true
    ) {
        if (viewPager == null) {
            detachViewPager()
            return
        }
        check(mediator == null) {
            "TabLayout has setup with viewpager,if you want reattach,please call detachViewPager method first."
        }
        this.viewPager = viewPager
        mediator =
            LeanbackTabLayoutMediator(this, viewPager, tabConfigurationStrategy, autoRefresh).also {
                it.attach()
            }
    }

    fun detachViewPager() {
        this.viewPager = null
        mediator?.detach()
        mediator = null
    }


    /**
     * Create and return a new [Tab]. You need to manually add this using [.addTab]
     * or a related method.
     *
     * @return A new Tab
     * @see .addTab
     */
    override fun newTab(): Tab {
        val tab = super.newTab()
        setupTabViewLeanbackMode(tab.view)
        return tab
    }

    override fun releaseFromTabPool(tab: Tab?): Boolean {
        return super.releaseFromTabPool(tab)
    }

    private fun setupTabViewLeanbackMode(view: View) {
        if (isLeanbackMode) {
            view.let {
                it.isFocusable = true
                it.isFocusableInTouchMode = true
                it.onFocusChangeListener = TabFocusChangeListener(
                    this,
                    viewPager
                )
            }
        } else {
            val listener = view.onFocusChangeListener
            if (listener != null && listener is TabFocusChangeListener) {
                view.onFocusChangeListener = null
            }
        }
    }

    private class TabFocusChangeListener constructor(
        val tabLayout: TabLayout,
        val viewPager: ViewPager?
    ) : OnFocusChangeListener {
        override fun onFocusChange(v: View, hasFocus: Boolean) {
            if (!hasFocus)
                return

            val tabStrip = tabLayout.getChildAt(0) as? LinearLayout ?: return
            if (viewPager == null) {
                val currentItem = tabLayout.selectedTabPosition
                //未绑定viewpger，则直接让tab选中即可：没有通过setupWithViewPager绑定会出现该情况
                for (i in 0 until tabStrip.childCount) {
                    if (v == tabStrip.getChildAt(i) && currentItem != i) {
                        tabLayout.selectTab(tabLayout.getTabAt(i))
                        return
                    }
                }
            } else {
                val currentItem = viewPager.currentItem

                for (i in 0 until tabStrip.childCount) {
                    if (v == tabStrip.getChildAt(i) && i != currentItem) {
                        //当前选中位置不同，让viewpager切换选项卡
                        viewPager.setCurrentItem(i, true)
                        return
                    }
                }
            }
        }
    }

}
