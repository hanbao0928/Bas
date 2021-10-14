package com.bas.sample.leanbacktab

import androidx.viewpager.widget.PagerAdapter
import com.bas.android.leanback.tab.TabConfigurationStrategy
import com.bas.core.android.util.getDrawable
import com.google.android.material.tabs.TabLayout

/**
 * Created by Lucio on 2021/10/12.
 */
class TextIconTabActivity : BaseTabActivity() {


    override fun createTabConfigurationStrategy(adapter: PagerAdapter?): TabConfigurationStrategy {

        return object : TabConfigurationStrategy{
            /**
             * Called to configure the tab for the page at the specified position. Typically calls [ ][TabLayout.Tab.setText], but any form of styling can be applied.
             *
             * @param tab The Tab which should be configured to represent the title of the item at the given
             * position in the data set.
             * @param position The position of the item within the adapter's data set.
             */
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                tab.text = "Tab$position"
                tab.icon = getDrawable("ic_tab_${position + 1}");
            }
        }

    }
}