package com.bas.sample.leanbacktab

import androidx.viewpager.widget.PagerAdapter
import com.bas.android.leanback.tab.TabConfigurationStrategy

/**
 * Created by Lucio on 2021/10/12.
 */
class TextTabActivity : BaseTabActivity() {


    override fun createTabConfigurationStrategy(adapter: PagerAdapter): TabConfigurationStrategy {

        val tabTitles = mutableListOf<CharSequence>()
        for (i in 0 until adapter.count) {
            tabTitles.add("Tab$i")
        }
        return TabConfigurationStrategy.TextStrategy(tabTitles)
    }
}