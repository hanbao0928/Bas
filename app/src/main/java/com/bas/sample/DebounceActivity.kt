package com.bas.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import bas.droid.core.ui.toast
import bas.droid.core.view.extensions.debounceClick
import bas.droid.core.view.extensions.setOnDebounceClickListener
import com.bas.R

/**
 * Created by Lucio on 2021/12/1.
 */
class DebounceActivity:AppCompatActivity(R.layout.activity_debounce) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.debounce_flow).debounceClick(this){
            println("flow click invoke")
            toast("flow click invoke")
        }

        findViewById<View>(R.id.debounce_listener).setOnDebounceClickListener{
            println("listener click invoke")
            toast("listener click invoke")
        }
    }


}