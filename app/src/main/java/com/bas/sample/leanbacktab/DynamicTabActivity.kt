package com.bas.sample.leanbacktab

import android.os.Bundle
import android.widget.Toast
import com.bas.databinding.LeanbackTabMenuLayoutBinding

/**
 * Created by Lucio on 2021/10/12.
 */
class DynamicTabActivity : BaseTabActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val menuLayout = LeanbackTabMenuLayoutBinding.inflate(layoutInflater,binding.root,false)

        binding.root.addView(menuLayout.root,0)
        menuLayout.setAdapterBtn.setOnClickListener {
            super.bindAdapter()
        }
        menuLayout.changeCntBtn.setOnClickListener {
            if(isAdapterInit()){
                adapter.setChildCount(6)
            }else{
                Toast.makeText(this,"Adapter还未初始化",Toast.LENGTH_SHORT).show()
            }

        }
        menuLayout.resetAdapterBtn.setOnClickListener {
            adapter = MyPagerAdapter(supportFragmentManager)
            binding.tabViewPager.adapter = adapter
        }

        menuLayout.detachAdapterBtn.setOnClickListener {
            binding.tabViewPager.adapter = null
        }

    }

    override fun bindAdapter() {
        //NO-op：暂时不绑定adapter
    }


}