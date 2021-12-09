package com.bas.sample.effectlayout

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import bas.android.core.util.dp
import bas.leanback.ui.LeanbackEffectLayout
import com.bas.R

/**
 * Created by Lucio on 2021/12/1.
 */
class EffectLayoutActivity:AppCompatActivity(R.layout.activity_effect_layout) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.circle1).setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus)
                v.bringToFront()
        }

        findViewById<View>(R.id.circle2).setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus)
                v.bringToFront()
        }

    }

    fun onBtnClick(view:View){
        when(view.id){
            R.id.test_remove->{
                val v1 = findViewById<LeanbackEffectLayout>(R.id.square)
                v1.removeEffectView()
            }
            R.id.test_front->{
                findViewById<View>(R.id.circle1_image).bringToFront()
            }
            R.id.test_size->{
                findViewById<View>(R.id.circle2_image).updateLayoutParams {
                 width = width + 8.dp
                 height = height + 8.dp
                }
            }
        }
    }
}