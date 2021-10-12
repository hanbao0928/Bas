package com.bas.sample.leanbacktab

import android.graphics.drawable.Drawable
import androidx.viewpager.widget.PagerAdapter
import com.bas.android.leanback.tab.TabConfigurationStrategy
import com.bas.core.android.util.getDrawable

/**
 * Created by Lucio on 2021/10/12.
 */
class TextIconTabActivity : BaseTabActivity() {


    override fun createTabConfigurationStrategy(adapter: PagerAdapter): TabConfigurationStrategy {

        val tabTitles = mutableListOf<CharSequence>()
        val tabIcons = mutableListOf<Drawable?>()
        for (i in 0 until adapter.count) {
            tabTitles.add("Tab$i")
            tabIcons.add(getDrawable("ic_tab_${i + 1}"))
        }
        return TabConfigurationStrategy.TextIconStrategy(tabTitles,tabIcons)
    }
}