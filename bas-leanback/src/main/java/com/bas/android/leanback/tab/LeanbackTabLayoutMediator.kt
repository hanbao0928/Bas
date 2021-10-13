package com.bas.android.leanback.tab

import android.database.DataSetObserver
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import java.lang.ref.WeakReference

/**
 * Created by Lucio on 2021/10/11.
 * @param autoRefresh 是否自动刷新，即[ViewPager.setAdapter]设置的[androidx.viewpager.widget.PagerAdapter]数据发生变化时，tab是否对应发生变化
 * @param tabConfigurationStrategy 用于配置[TabLayout.Tab]的样式：即控制TabLayout添加的item效果;
 * 参考[TabConfigurationStrategy.TextStrategy]、
 * [TabConfigurationStrategy.TextIconStrategy]、
 * [TabConfigurationStrategy.CustomViewStrategy]
 */
class LeanbackTabLayoutMediator(
    private val tabLayout: TabLayout,
    internal val viewPager: ViewPager,
    private var tabConfigurationStrategy: TabConfigurationStrategy,
    private var autoRefresh: Boolean = true
) {
    private var attached = false
    private var onPageChangeCallback: TabLayoutOnPageChangeListener
    private val onTabSelectedListener: ViewPagerOnTabSelectedListener
    private var pagerAdapterDataSetObserver: PagerAdapterDataSetObserver
    private val adapterChangeListener: AdapterChangeListener
    private var pagerAdapter: PagerAdapter? = null

    init {
        onTabSelectedListener = ViewPagerOnTabSelectedListener(viewPager)
        pagerAdapterDataSetObserver = PagerAdapterDataSetObserver()
        onPageChangeCallback = TabLayoutOnPageChangeListener(tabLayout)

        adapterChangeListener = AdapterChangeListener()
        viewPager.addOnAdapterChangeListener(adapterChangeListener)
    }

    /**
     * Link the TabLayout and the ViewPager2 together. Must be called after ViewPager2 has an adapter
     * set. To be called on a new instance of TabLayoutMediator or if the ViewPager2's adapter
     * changes.
     *
     * @throws IllegalStateException If the mediator is already attached, or the ViewPager has no
     * adapter.
     */
    fun attach() {
        check(!attached) { "TabLayoutMediator is already attached" }
        attached = true
        onPageChangeCallback.reset()
        viewPager.addOnPageChangeListener(onPageChangeCallback)
        tabLayout.addOnTabSelectedListener(onTabSelectedListener)
        setPagerAdapter(viewPager.adapter,autoRefresh)
        // Now update the scroll position to match the ViewPager's current item
        tabLayout.setScrollPosition(viewPager.currentItem, 0f, true)
    }

    /**
     * Unlink the TabLayout and the ViewPager. To be called on a stale TabLayoutMediator if a new one
     * is instantiated, to prevent holding on to a view that should be garbage collected.
     */
    fun detach() {
        tabLayout.removeOnTabSelectedListener(onTabSelectedListener)
        viewPager.removeOnPageChangeListener(onPageChangeCallback)
        setPagerAdapter(null,false)
        attached = false
    }

    private fun setPagerAdapter(adapter: PagerAdapter?, addObserver: Boolean) {
        // If we already have a PagerAdapter, unregister our observer
        pagerAdapter?.unregisterDataSetObserver(pagerAdapterDataSetObserver)
        pagerAdapter = adapter
        if (addObserver && adapter != null) {
            adapter.registerDataSetObserver(pagerAdapterDataSetObserver)
        }
        // Finally make sure we reflect the new adapter
        populateTabsFromPagerAdapter()
    }

    internal fun populateTabsFromPagerAdapter() {
        tabLayout.removeAllTabs()

        val adapterCount: Int = this.pagerAdapter?.count ?: return
        for (i in 0 until adapterCount) {
            val tab = tabLayout.newTab()
            tabConfigurationStrategy.onConfigureTab(tab, i)
            tabLayout.addTab(tab, false)
        }
        // Make sure we reflect the currently set ViewPager item
        if (adapterCount > 0) {
            val lastItem = tabLayout.tabCount - 1
            val currItem = viewPager.currentItem.coerceAtMost(lastItem)
            if (currItem != tabLayout.selectedTabPosition) {
                tabLayout.selectTab(tabLayout.getTabAt(currItem))
            }
        }
    }

    /**
     * A [ViewPager.OnPageChangeListener] class which contains the necessary calls back to the
     * provided [TabLayout] so that the tab position is kept in sync.
     *
     * This class stores the provided TabLayout weakly, meaning that you can use [ViewPager.addOnPageChangeListener] without removing the listener and not cause a
     * leak.
     */
    internal class TabLayoutOnPageChangeListener(tabLayout: TabLayout) :
        ViewPager.OnPageChangeListener {
        private val tabLayoutRef: WeakReference<TabLayout> = WeakReference(tabLayout)
        private var previousScrollState = 0
        private var scrollState = 0
        override fun onPageScrollStateChanged(state: Int) {
            previousScrollState = scrollState
            scrollState = state
        }

        override fun onPageScrolled(
            position: Int, positionOffset: Float, positionOffsetPixels: Int
        ) {
            tabLayoutRef.get()?.let { tabLayout ->
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

    }

    /**
     * A [TabLayout.OnTabSelectedListener] class which contains the necessary calls back to the
     * provided [ViewPager] so that the tab position is kept in sync.
     */
    internal class ViewPagerOnTabSelectedListener(private val viewPager: ViewPager) :
        TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            viewPager.currentItem = tab.position
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            // No-op
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
            // No-op
        }
    }

    /**
     * [ViewPager]绑定的[PagerAdapter]发生变化时重新绑定
     */
    private inner class AdapterChangeListener : ViewPager.OnAdapterChangeListener {
        override fun onAdapterChanged(
            viewPager: ViewPager,
            oldAdapter: PagerAdapter?,
            newAdapter: PagerAdapter?
        ) {
            if (this@LeanbackTabLayoutMediator.viewPager != viewPager)
                return
            setPagerAdapter(newAdapter,autoRefresh)
        }
    }

    /**
     * 观察[PagerAdapter]数据发生变化时刷新Tab
     */
    private inner class PagerAdapterDataSetObserver : DataSetObserver() {
        override fun onChanged() {
            populateTabsFromPagerAdapter()
        }

        override fun onInvalidated() {
            populateTabsFromPagerAdapter()
        }
    }
}