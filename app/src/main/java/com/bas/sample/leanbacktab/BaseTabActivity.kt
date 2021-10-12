package com.bas.sample.leanbacktab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.bas.android.leanback.tab.LeanbackTabLayoutMediator
import com.bas.android.leanback.tab.TabConfigurationStrategy
import com.bas.core.android.util.Logger
import com.bas.databinding.LeanbackTabActivityBinding

/**
 * Created by Lucio on 2021/10/12.
 */
abstract class BaseTabActivity : AppCompatActivity() {

    lateinit var binding: LeanbackTabActivityBinding

    lateinit var mediator: LeanbackTabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LeanbackTabActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tabViewPager.adapter = createPagerAdapter()
        binding.tabLayout.setFocusOutEnabled(false)
        mediator = LeanbackTabLayoutMediator(
            binding.tabLayout,
            binding.tabViewPager,
            true,
            createTabConfigurationStrategy(binding.tabViewPager.adapter!!)
        )
        mediator.attach()

        window.decorView.viewTreeObserver.addOnGlobalFocusChangeListener { oldFocus, newFocus ->
            Logger.d("TabActivity","oldFoucs=$oldFocus \nnewFocus=$newFocus")
        }
    }

    fun createPagerAdapter(): PagerAdapter {
        return object :
            FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            /**
             * Return the number of views available.
             */
            override fun getCount(): Int {
                return 4
            }

            /**
             * Return the Fragment associated with a specified position.
             */
            override fun getItem(position: Int): Fragment {
                return TabFragment()
            }
        }
    }

    abstract fun createTabConfigurationStrategy(adapter: PagerAdapter): TabConfigurationStrategy
}