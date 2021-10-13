package com.bas.sample.leanbacktab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.bas.android.leanback.tab.LeanbackTabLayoutMediator
import com.bas.android.leanback.tab.TabConfigurationStrategy
import com.bas.core.android.util.Logger
import com.bas.databinding.LeanbackTabActivityBinding
import com.google.android.material.tabs.TabLayout

/**
 * Created by Lucio on 2021/10/12.
 */
abstract class BaseTabActivity : AppCompatActivity() {

    lateinit var binding: LeanbackTabActivityBinding
    lateinit var adapter:MyPagerAdapter
    lateinit var mediator: LeanbackTabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LeanbackTabActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = MyPagerAdapter(supportFragmentManager)
        binding.tabViewPager.adapter = adapter
        binding.tabLayout.setFocusOutEnabled(false)
        mediator = LeanbackTabLayoutMediator(
            binding.tabLayout,
            binding.tabViewPager,
            createTabConfigurationStrategy(binding.tabViewPager.adapter!!)
        )
        mediator.attach()

        window.decorView.viewTreeObserver.addOnGlobalFocusChangeListener { oldFocus, newFocus ->
            Logger.d("TabActivity","oldFoucs=$oldFocus \nnewFocus=$newFocus")
        }

        binding.tabLayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            /**
             * Called when a tab enters the selected state.
             *
             * @param tab The tab that was selected
             */
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Logger.d("TabActivity","onTabSelected=$tab")
            }

            /**
             * Called when a tab exits the selected state.
             *
             * @param tab The tab that was unselected
             */
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Logger.d("TabActivity","onTabUnselected=$tab")
            }

            /**
             * Called when a tab that is already selected is chosen again by the user. Some applications may
             * use this action to return to the top level of a category.
             *
             * @param tab The tab that was reselected.
             */
            override fun onTabReselected(tab: TabLayout.Tab?) {
                Logger.d("TabActivity","onTabReselected=$tab")
            }

        })
    }


    class  MyPagerAdapter: FragmentPagerAdapter {
        constructor(fm: FragmentManager) : super(fm)
        constructor(fm: FragmentManager, behavior: Int) : super(fm, behavior)

        private var childCount:Int = 4

        fun setChildCount(count:Int){
            this.childCount = count
            notifyDataSetChanged()
        }
        /**
         * Return the number of views available.
         */
        override fun getCount(): Int {
            return childCount
        }

        /**
         * Return the Fragment associated with a specified position.
         */
        override fun getItem(position: Int): Fragment {
            return TabFragment()
        }
    }

    abstract fun createTabConfigurationStrategy(adapter: PagerAdapter): TabConfigurationStrategy
}