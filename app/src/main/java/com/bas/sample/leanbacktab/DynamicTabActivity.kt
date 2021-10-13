package com.bas.sample.leanbacktab

import android.os.Bundle
import android.widget.Button
import androidx.viewpager.widget.PagerAdapter
import com.bas.android.leanback.tab.TabConfigurationStrategy
import com.bas.databinding.LeanbackTabMenuLayoutBinding

/**
 * Created by Lucio on 2021/10/12.
 */
class DynamicTabActivity : BaseTabActivity() {

    private lateinit var strategy: TabConfigurationStrategy.TextStrategy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val menuLayout = LeanbackTabMenuLayoutBinding.inflate(layoutInflater,binding.root,false)

        binding.root.addView(menuLayout.root,0)
        menuLayout.changeCntBtn.setOnClickListener {
            val tabTitles = mutableListOf<CharSequence>()
            for (i in 0 until 6) {
                tabTitles.add("Tab$i")
            }
            strategy.update(tabTitles)
            adapter.setChildCount(6)
        }
        menuLayout.resetAdapterBtn.setOnClickListener {
            adapter = MyPagerAdapter(supportFragmentManager)
            binding.tabViewPager.adapter = adapter
        }

    }

    override fun createTabConfigurationStrategy(adapter: PagerAdapter): TabConfigurationStrategy {

        val tabTitles = mutableListOf<CharSequence>()
        for (i in 0 until adapter.count) {
            tabTitles.add("Tab$i")
        }
        strategy =  TabConfigurationStrategy.TextStrategy(tabTitles)
        return strategy
    }
}