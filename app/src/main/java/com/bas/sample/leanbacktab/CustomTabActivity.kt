package com.bas.sample.leanbacktab

import androidx.viewpager.widget.PagerAdapter
import bas.leanback.tab.TabConfigurationStrategy

/**
 * Created by Lucio on 2021/10/12.
 */
class CustomTabActivity : BaseTabActivity() {

    override fun createTabConfigurationStrategy(adapter: PagerAdapter?): TabConfigurationStrategy {
        return TabConfigurationStrategy.CustomViewStrategy(CustomTabViewFactory(this))
    }
}