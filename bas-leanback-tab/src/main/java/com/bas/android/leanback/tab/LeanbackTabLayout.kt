package com.bas.android.leanback.tab

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.FocusFinder
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.tabs.TabLayout
import java.util.*

/**
 * Created by Lucio on 2021/10/11.
 *
 * TV和Phone使用TabLayout的最大区别在于交互上：
 *  Phone上是通过Tab的点击进行ViewPager的翻页
 *  TV上应该是通过Tab的焦点获取进行ViewPager的翻页
 *
 *  更多查看ReadMe
 */
class LeanbackTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.tabStyle
) : TabLayout(context, attrs, defStyleAttr) {

    private var viewPager: ViewPager? = null
    private var focusOutEnabled: Boolean = false
    private var focusMemoryEnabled: Boolean = true

    /**
     * 是否启用TabView选中/取消选中时，将TabView的[View.isActivated]状态传递给包含的所有ChildView
     */
    private var duplicateTabViewState: Boolean = false
    private lateinit var duplicateTabViewStateListener: DuplicateTabViewStateOnTabSelected

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

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.LeanbackTabLayout)
        focusOutEnabled = ta.getBoolean(R.styleable.LeanbackTabLayout_focusOutEnabled_lbt, false)
        focusMemoryEnabled =
            ta.getBoolean(R.styleable.LeanbackTabLayout_focusMemoryEnabled_lbt, true)
        isLeanbackMode = ta.getBoolean(R.styleable.LeanbackTabLayout_isLeanbackMode_lbt, true)
        duplicateTabViewState = ta.getBoolean(
            R.styleable.LeanbackTabLayout_duplicateTabViewState_lbt,
            false
        )
        ta.recycle()

        if (duplicateTabViewState) {
            duplicateTabViewStateListener = DuplicateTabViewStateOnTabSelected()
            addOnTabSelectedListener(duplicateTabViewStateListener)
        }
    }

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

    private fun log(msg: String) {
        Log.d("LeanbackTab", msg)
    }

    /**
     * Find the nearest view in the specified direction that wants to take
     * focus.
     *
     * 内部寻找焦点
     *
     * @param focused The view that currently has focus
     * @param direction One of FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, and
     * FOCUS_RIGHT, or 0 for not applicable.
     */
    override fun focusSearch(focused: View?, direction: Int): View? {
        //如果允许焦点移出、或者不是点击遥控器左右键、或者当前为非固定Tab模式并且可以滚动（tab的内容不足整个TabLayout）
        if (focusOutEnabled//允许焦点移除
            || (direction != FOCUS_LEFT && direction != FOCUS_RIGHT)//不是按的左、右键
//            || (tabMode != MODE_FIXED && canScroll())
        ) {
            log("focusSearch = super  focused=$focused direction = $direction")
            return super.focusSearch(focused, direction)
        } else {
            val ss = FocusFinder.getInstance().findNextFocus(this, focused, direction) ?: null
            log("focusSearch = $ss")
            return ss
        }
    }

    /**
     * 是否启用焦点记忆：获取焦点时让之前已选中的tab获取焦点
     */
    fun setFocusMemoryEnabled(enableFocusMemory: Boolean) {
        this.focusMemoryEnabled = enableFocusMemory
    }

    override fun addFocusables(views: ArrayList<View>?, direction: Int, focusableMode: Int) {
        if (views == null   //不会出现这种情况，只是重写方法时[views]参数可空，做个预防
            || tabCount <= 0 //当前还未添加任何tab
            || !focusMemoryEnabled//未启用焦点记忆
            || hasFocus() //当前焦点在内部,不用指定特定的可以获取焦点的view
        ) {
            log("addFocusables = super")
            return super.addFocusables(views, direction, focusableMode)
        }

        val strategyTab =
            getTabAt(selectedTabPosition) ?: getTabAt(0)

        if (strategyTab == null //理论上不会出现这个情况，前面已经过滤了tabCount<=0的情况
            || !strategyTab.view.canTakeFocus()// 不能获取焦点，放弃焦点记忆
        ) {
            return super.addFocusables(views, direction, focusableMode)
        }

        if (this.canTakeFocus())
            views.add(this)
        views.add(strategyTab.view)

    }

    /**
     * When looking for focus in children of a scroll view, need to be a little
     * more careful not to give focus to something that is scrolled off screen.
     *
     * This is more expensive than the default [android.view.ViewGroup]
     * implementation, otherwise this behavior might have been made the default.
     */
    override fun onRequestFocusInDescendants(
        direction: Int,
        previouslyFocusedRect: Rect?
    ): Boolean {
        //优化child请求焦点规则：调用TabLayout.requestFocus()请求焦点时，在不影响super规则的前提下，
        //如果未找到合适的child获取焦点，则尝试让已选择的tab获取焦点
        val handled = super.onRequestFocusInDescendants(direction, previouslyFocusedRect)
        if (handled || selectedTabPosition < 0)
            return handled
        val selectedTab = getTabAt(selectedTabPosition) ?: return handled
        if (selectedTab.view.canTakeFocus() && !selectedTab.view.isFocused && selectedTab.view.requestFocus()) {
            return true
        }
        return handled
    }

    private fun View.canTakeFocus(): Boolean {
        return isFocusable && isVisible && isEnabled
    }

//    /**
//     * @return Returns true this HorizontalScrollView can be scrolled
//     */
//    private fun canScroll(): Boolean {
//        val child = getChildAt(0)
//        if (child != null) {
//            val childWidth = child.width
//            return width < childWidth + paddingLeft + paddingRight
//        }
//        return false
//    }

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
     * @param tabConfigurationStrategy tab配置策略，用于自定义Tab样式；
     * [TabConfigurationStrategy.TextStrategy]:文本tab策略
     * [TabConfigurationStrategy.ViewPagerStrategy]:（系统TabLayout与ViewPager联用时的规则相似） 使用ViewPager的adapter提供的getPageTitle方法创建文本Tab策略
     * [TabConfigurationStrategy.TextIconStrategy]:文本+图标策略
     * [TabConfigurationStrategy.CustomViewStrategy]：自定义view策略,[TabConfigurationStrategy.CustomViewFactory]
     * 如果以上策略不能满足需求，可以自定义实现[TabConfigurationStrategy]。
     * @param autoRefresh [ViewPager.getAdapter]数据改变时是否自动刷新Tab
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

    fun isAttachMediator(): Boolean {
        return mediator?.attached ?: false
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

                //用于解决TabView失去焦点但选中时isActivated状态传递给ChildView
                if (duplicateTabViewState && ::duplicateTabViewStateListener.isInitialized) {
                    it.onFocusChangeListener = ChainFocusListener(
                        TabFocusChangeListener(
                            this,
                            viewPager
                        ), duplicateTabViewStateListener
                    )
                } else {
                    it.onFocusChangeListener = TabFocusChangeListener(
                        this,
                        viewPager
                    )
                }
            }
        } else {
            val listener = view.onFocusChangeListener
            if (listener != null && listener is TabFocusChangeListener) {
                view.onFocusChangeListener = null
            }
        }
    }

    private open class TabFocusChangeListener constructor(
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

    /**
     * TabView选中/取消选中时，将TabView的[View.isActivated]状态传递给包含的所有ChildView
     */
    private class DuplicateTabViewStateOnTabSelected : OnTabSelectedListener,
        OnFocusChangeListener {

        private fun setChildActivatedRecursively(view: View, isActivated: Boolean) {
            view.isActivated = isActivated
            if (view is ViewGroup) {
                view.children.forEach {
                    setChildActivatedRecursively(it, isActivated)
                }
            }
        }

        private fun duplicateTabViewState(tabView: TabView?, isActivated: Boolean) {
            tabView?.isActivated = isActivated
            tabView?.children?.forEach {
                if (BadgeUtils.USE_COMPAT_PARENT) {
                    setChildActivatedRecursively(it, isActivated)
                } else {
                    it.isActivated = isActivated
                }
            }
        }

        /**
         * Called when a tab enters the selected state.
         *
         * @param tab The tab that was selected
         */
        override fun onTabSelected(tab: Tab?) {
//            duplicateTabViewState(tab?.view, true)
        }

        /**
         * Called when a tab exits the selected state.
         *
         * @param tab The tab that was unselected
         */
        override fun onTabUnselected(tab: Tab?) {
//            duplicateTabViewState(tab?.view, false)
        }

        /**
         * Called when a tab that is already selected is chosen again by the user. Some applications may
         * use this action to return to the top level of a category.
         *
         * @param tab The tab that was reselected.
         */
        override fun onTabReselected(tab: Tab?) {
        }

        /**
         * 失去焦点的时候也需要让child的active状态设置为false
         * Called when the focus state of a view has changed.
         *
         * @param v The view whose state has changed.
         * @param hasFocus The new focus state of v.
         */
        override fun onFocusChange(v: View?, hasFocus: Boolean) {
//            if(!hasFocus){
            duplicateTabViewState(v as? TabView, hasFocus)
//            }
        }

    }

    private class ChainFocusListener(vararg val listeners: OnFocusChangeListener) :
        OnFocusChangeListener {
        /**
         * Called when the focus state of a view has changed.
         *
         * @param v The view whose state has changed.
         * @param hasFocus The new focus state of v.
         */
        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            listeners.forEach {
                it.onFocusChange(v, hasFocus)
            }
        }

    }

}
