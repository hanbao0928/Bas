package com.bas.sample.leanbacktab

import androidx.viewpager.widget.PagerAdapter
import bas.leanback.tab.TabConfigurationStrategy
import bas.droid.core.util.getDrawable
import com.google.android.material.tabs.TabLayout

/**
 * Created by Lucio on 2021/10/12.
 */
class MixedTabActivity : BaseTabActivity() {

    override fun createTabConfigurationStrategy(adapter: PagerAdapter?): TabConfigurationStrategy {
        val textStrategy = TabConfigurationStrategy.ViewPagerStrategy(binding.tabViewPager)
        val textIconStrategy = object : TabConfigurationStrategy{
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                tab.text = "Tab$position"
                tab.icon = getDrawable("ic_tab_${position + 1}");
            }
        }

        val customStrategy =
            TabConfigurationStrategy.CustomViewStrategy(CustomTabViewFactory(this))

        return object : TabConfigurationStrategy {
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                if (position == 0) {
                    textStrategy.onConfigureTab(tab, position)
                } else if (position == 1) {
                    textIconStrategy.onConfigureTab(tab, position)
                    tab.orCreateBadge.apply {
                        maxCharacterCount = 99
                        number = 101
                        this.badgeGravity
                    }
                } else {
                    customStrategy.onConfigureTab(tab, position)
                }
            }
        }
    }
}