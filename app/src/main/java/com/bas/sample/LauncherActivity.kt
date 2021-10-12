package com.bas.sample

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bas.R
import com.bas.sample.leanbacktab.IndexActivity

/**
 * Created by Lucio on 2021/10/12.
 */
class LauncherActivity() : AppCompatActivity(R.layout.launcher_activity) {

    fun onBtnClick(v:View){
        when(v.id){
            R.id.tab_test_btn->{
                startActivity(Intent(this,IndexActivity::class.java))
            }
        }
    }
}