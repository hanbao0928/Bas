package com.bas.sample

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import bas.droid.core.ui.toast
import bas.droid.core.view.extensions.debounceClick
import bas.droid.core.view.extensions.searchUseCase
import bas.droid.core.view.extensions.setOnDebounceClickListener
import bas.lib.core.date.dateTimeFormatCN
import bas.lib.core.date.now
import com.bas.R
import kotlinx.coroutines.delay

/**
 * Created by Lucio on 2021/12/1.
 */
class DebounceActivity : AppCompatActivity(R.layout.activity_debounce) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.debounce_flow).debounceClick(this) {
            println("flow click invoke")
            toast("flow click invoke")
        }

        findViewById<View>(R.id.debounce_listener).setOnDebounceClickListener {
            println("listener click invoke")
            toast("listener click invoke")
        }

        findViewById<EditText>(R.id.textchangeflow).searchUseCase(lifecycleScope, 1000, block = {
            println("Debounce:执行搜索逻辑${it} time=${now().dateTimeFormatCN()}")
            delay(2000)
            println("Debounce:执行搜索逻辑${it}完成 time=${now().dateTimeFormatCN()}")
        }, start = {
            println("Debounce:start")
        }, success = {
            println("Debounce：success，执行渲染 $it")
        }, error = {
            println("Debounce:error 搜索异常")
        }, complete = {
            println("Debounce:complete")
        })

    }


}