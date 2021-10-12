package com.bas.android.leanback.tab

import android.content.Context
import android.database.DataSetObserver
import android.util.AttributeSet
import android.view.FocusFinder
import android.view.View
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnAdapterChangeListener
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.tabs.TabLayout
import java.lang.ref.WeakReference
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

    private var viewPager: ViewPager? = null
    private var currentVpSelectedListener: ViewPagerOnTabSelectedListener? = null
    private var pageChangeListener: TabLayoutOnPageChangeListener? = null
    private var adapterChangeListener: AdapterChangeListener? = null
    private val adapterDataSetObserver = AdapterDataSetObserver(this)

    private val tabStrip get() = getChildAt(0) as? LinearLayout

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

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

    /**
     * 初始化ViewPager
     */
    fun setupWithLeanbackViewPager(viewPager: LeanbackViewPager?, autoRefresh: Boolean = true) {
        this.viewPager?.let { preVP ->
            pageChangeListener?.let {
                preVP.removeOnPageChangeListener(it)
            }
            adapterChangeListener?.let {
                preVP.removeOnAdapterChangeListener(it)
            }
        }

        currentVpSelectedListener?.let {
            removeOnTabSelectedListener(it)
        }
        currentVpSelectedListener = null

        if (viewPager != null) {
            this.viewPager = viewPager
            if (pageChangeListener == null) {
                pageChangeListener = TabLayoutOnPageChangeListener(this)
            }
//            pageChangeListener.reset()
        }
        this.viewPager?.adapter?.unregisterDataSetObserver(adapterDataSetObserver)
        this.viewPager = viewPager

        this.viewPager?.adapter?.registerDataSetObserver(adapterDataSetObserver)
    }

    override fun setupWithViewPager(viewPager: ViewPager?) {
        super.setupWithViewPager(viewPager)
        if (this.viewPager != null && this.viewPager!!.adapter != null) {
            this.viewPager!!.adapter!!.unregisterDataSetObserver(adapterDataSetObserver)
        }
        this.viewPager = viewPager
        if (this.viewPager != null && this.viewPager!!.adapter != null) {
            this.viewPager!!.adapter!!.registerDataSetObserver(adapterDataSetObserver)
        }
    }

    /**
     * 设置ViewPager适配器
     */
    private fun setPagerAdapter(adapter: PagerAdapter?, addObserver: Boolean) {

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        updatePageTabs()
    }

    /**
     * 更新tab
     */
    internal fun updatePageTabs() {
        val tabStrip = tabStrip ?: return
        val tabCount = tabStrip.childCount
        for (i in 0 until tabCount) {
            val tabView = tabStrip.getChildAt(i)
            tabView.isFocusable = true
            tabView.onFocusChangeListener = TabFocusChangeListener(
                this,
                viewPager
            )
        }
    }

    /**
     * A [TabLayout.OnTabSelectedListener] class which contains the necessary calls back to the
     * provided [ViewPager] so that the tab position is kept in sync.
     */
    class ViewPagerOnTabSelectedListener(private val viewPager: ViewPager) : OnTabSelectedListener {
        override fun onTabSelected(tab: Tab) {
            viewPager.currentItem = tab.position
        }

        override fun onTabUnselected(tab: Tab?) {
            // No-op
        }

        override fun onTabReselected(tab: Tab?) {
            // No-op
        }
    }

    /**
     * A [ViewPager.OnPageChangeListener] class which contains the necessary calls back to the
     * provided [TabLayout] so that the tab position is kept in sync.
     *
     * This class stores the provided TabLayout weakly, meaning that you can use [ViewPager.addOnPageChangeListener] without removing the listener and not cause a
     * leak.
     */
    private class TabLayoutOnPageChangeListener(tabLayout: TabLayout) :
        OnPageChangeListener {
        private val tabLayoutRef: WeakReference<TabLayout>
        private var previousScrollState = 0
        private var scrollState = 0
        override fun onPageScrollStateChanged(state: Int) {
            previousScrollState = scrollState
            scrollState = state
        }

        override fun onPageScrolled(
            position: Int, positionOffset: Float, positionOffsetPixels: Int
        ) {
            val tabLayout = tabLayoutRef.get()
            if (tabLayout != null) {
                // Only update the text selection if we're not settling, or we are settling after
                // being dragged
                val updateText =
                    scrollState != ViewPager.SCROLL_STATE_SETTLING || previousScrollState == ViewPager.SCROLL_STATE_DRAGGING
                // Update the indicator if we're not settling after being idle. This is caused
                // from a setCurrentItem() call and will be handled by an animation from
                // onPageSelected() instead.
                val updateIndicator =
                    !(scrollState == ViewPager.SCROLL_STATE_SETTLING && previousScrollState == ViewPager.SCROLL_STATE_IDLE)
                tabLayout.setScrollPosition(position, positionOffset, updateText, updateIndicator)
            }
        }

        override fun onPageSelected(position: Int) {
            val tabLayout = tabLayoutRef.get()
            if (tabLayout != null && tabLayout.selectedTabPosition != position && position < tabLayout.tabCount) {
                // Select the tab, only updating the indicator if we're not being dragged/settled
                // (since onPageScrolled will handle that).
                val updateIndicator = (scrollState == ViewPager.SCROLL_STATE_IDLE
                        || (scrollState == ViewPager.SCROLL_STATE_SETTLING
                        && previousScrollState == ViewPager.SCROLL_STATE_IDLE))
                tabLayout.selectTab(tabLayout.getTabAt(position), updateIndicator)
            }
        }

        fun reset() {
            scrollState = ViewPager.SCROLL_STATE_IDLE
            previousScrollState = scrollState
        }

        init {
            tabLayoutRef = WeakReference(tabLayout)
        }
    }

    private inner class AdapterChangeListener constructor() : OnAdapterChangeListener {

        private var autoRefresh = false

        override fun onAdapterChanged(
            viewPager: ViewPager,
            oldAdapter: PagerAdapter?,
            newAdapter: PagerAdapter?
        ) {

            if (this@LeanbackTabLayout.viewPager == viewPager) {
                setPagerAdapter(newAdapter, autoRefresh)
            }
        }

        fun setAutoRefresh(autoRefresh: Boolean) {
            this.autoRefresh = autoRefresh
        }
    }

    private class AdapterDataSetObserver constructor(val leanbackTabLayout: LeanbackTabLayout) :
        DataSetObserver() {

        override fun onChanged() {
            leanbackTabLayout.updatePageTabs()
        }

        override fun onInvalidated() {
            leanbackTabLayout.updatePageTabs()
        }
    }

    private class TabFocusChangeListener constructor(
        var leanbackTabLayout: LeanbackTabLayout,
        var viewPager: ViewPager?
    ) :
        OnFocusChangeListener {
        override fun onFocusChange(v: View, hasFocus: Boolean) {
            if (hasFocus) {
                val tabStrip = leanbackTabLayout.tabStrip ?: return
                viewPager?.let {
                    for (i in 0 until tabStrip.childCount) {
                        if (v == tabStrip.getChildAt(i)) {
                            it.setCurrentItem(i, true)
                            return@let
                        }
                    }
                }
            }
        }
    }

}
