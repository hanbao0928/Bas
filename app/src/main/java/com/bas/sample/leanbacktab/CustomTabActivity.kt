package com.bas.sample.leanbacktab

import android.graphics.drawable.Drawable
import android.view.View
import androidx.viewpager.widget.PagerAdapter
import com.bas.android.leanback.tab.TabConfigurationStrategy
import com.bas.core.android.util.getDrawable
import com.google.android.material.tabs.TabLayout

/**
 * Created by Lucio on 2021/10/12.
 */
class CustomTabActivity : BaseTabActivity() {

    override fun createTabConfigurationStrategy(adapter: PagerAdapter): TabConfigurationStrategy {
        return TabConfigurationStrategy.CustomViewStrategy(CustomTabViewFactory(this,adapter))
    }
}