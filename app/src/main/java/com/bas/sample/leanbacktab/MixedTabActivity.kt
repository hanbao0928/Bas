package com.bas.sample.leanbacktab

import android.graphics.drawable.Drawable
import androidx.viewpager.widget.PagerAdapter
import com.bas.android.leanback.tab.TabConfigurationStrategy
import com.bas.core.android.util.getDrawable
import com.google.android.material.tabs.TabLayout

/**
 * Created by Lucio on 2021/10/12.
 */
class MixedTabActivity : BaseTabActivity() {

    override fun createTabConfigurationStrategy(adapter: PagerAdapter): TabConfigurationStrategy {

        val tabTitles = mutableListOf<CharSequence>()
        val tabIcons = mutableListOf<Drawable?>()
        for (i in 0 until adapter.count) {
            tabTitles.add("Tab$i")
            tabIcons.add(getDrawable("ic_tab_${i + 1}"))
        }
        val textStrategy = TabConfigurationStrategy.TextStrategy(tabTitles)
        val textIconStrategy = TabConfigurationStrategy.TextIconStrategy(tabTitles, tabIcons)

        val customStrategy =
            TabConfigurationStrategy.CustomViewStrategy(CustomTabViewFactory(this, adapter))
        return object : TabConfigurationStrategy {
            /**
             * Called to configure the tab for the page at the specified position. Typically calls [ ][TabLayout.Tab.setText], but any form of styling can be applied.
             *
             * @param tab The Tab which should be configured to represent the title of the item at the given
             * position in the data set.
             * @param position The position of the item within the adapter's data set.
             */
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