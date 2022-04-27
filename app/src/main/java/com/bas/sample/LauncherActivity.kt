package com.bas.sample

import andme.arch.app.FragmentContainerActivity
import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.bas.R
import com.bas.sample.effectlayout.EffectLayoutActivity
import com.bas.sample.imageloader.ImageLoaderTestFragment
import com.bas.sample.label.LabelLayoutActivity
import com.bas.sample.leanbacklayout.LeanbackLayoutActivity
import com.bas.sample.systemui.SystemUIFragment
import com.bas.tvtest.TVTestActivity

/**
 * Created by Lucio on 2021/10/12.
 */
class LauncherActivity() : AppCompatActivity(R.layout.launcher_activity) {

    fun onBtnClick(v: View) {
        when (v.id) {
            R.id.tab_test_btn -> {
                startActivity(Intent(this, IndexActivity::class.java))
            }
            R.id.leanback_layout_test_btn -> {
                this.startActivity(Intent(this, LeanbackLayoutActivity::class.java))
            }
            R.id.effect_layout_test_btn -> {
                this.startActivity(Intent(this, EffectLayoutActivity::class.java))
            }
            R.id.label_test_btn -> {
                this.startActivity(Intent(this, LabelLayoutActivity::class.java))
            }
            R.id.systemui_btn -> {
                val color =
                    Color.argb((0.2 * 255).toInt(), Color.RED.red, Color.RED.green, Color.RED.blue)
                startActivity(
                    FragmentContainerActivity.Builder(this, SystemUIFragment::class.java)
                        .setImmersiveStatusBar(color)
                        .build()
                )
            }

            R.id.imageloader_btn -> {
                startActivity(
                    FragmentContainerActivity.Builder(this, ImageLoaderTestFragment::class.java)
                        .setNormalStatusBar(Color.GREEN)
                        .build()
                )
            }
            R.id.tvtest_btn -> {
                this.startActivity(Intent(this, TVTestActivity::class.java))
            }
        }
    }
}