package com.bas.sample.leanbacktab

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bas.R
import com.bas.android.leanback.tab.TabConfigurationStrategy
import com.bas.core.android.util.getDrawable
import com.bas.core.android.util.layoutInflater
import com.google.android.material.tabs.TabLayout

/**
 * Created by Lucio on 2021/10/12.
 */
class CustomTabViewFactory(val ctx: Context) : TabConfigurationStrategy.CustomViewFactory() {

    override fun createCustomTabView(tab: TabLayout.Tab, position: Int): View {
        val view = ctx.layoutInflater.inflate(R.layout.leanback_tab_custom_view, tab.view, false)
        view.findViewById<TextView>(R.id.text1).text = "Tab$position"
        view.findViewById<ImageView>(R.id.icon)
            .setImageDrawable(ctx.getDrawable("ic_tab_${position + 1}"))
        return view
    }
}