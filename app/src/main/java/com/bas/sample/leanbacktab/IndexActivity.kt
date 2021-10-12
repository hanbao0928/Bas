package com.bas.sample.leanbacktab

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bas.R
import com.bas.databinding.LeanbackTestActivityBinding

/**
 * Created by Lucio on 2021/10/12.
 */
class IndexActivity : Activity() {

    lateinit var binding: LeanbackTestActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LeanbackTestActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun onBtnClick(v: View) {
        when (v.id) {
            R.id.text_tab_btn -> {
                startActivity(Intent(this,TextTabActivity::class.java))
            }
            R.id.text_icon_tab_btn -> {
                startActivity(Intent(this,TextIconTabActivity::class.java))
            }
            R.id.custom_tab_btn -> {
                startActivity(Intent(this,CustomTabActivity::class.java))
            }
            R.id.mixed_tab_btn -> {
                startActivity(Intent(this,MixedTabActivity::class.java))
            }
        }
    }
}