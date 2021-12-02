package com.bas.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bas.R
import com.bas.WeatherService
import com.bas.core.lang.orDefaultIfNullOrEmpty
import com.bas.databinding.LeanbackTestActivityBinding
import com.bas.sample.leanbacktab.*
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Lucio on 2021/10/12.
 */
class IndexActivity : Activity() {

    lateinit var binding: LeanbackTestActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LeanbackTestActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WeatherService()
            .requestData(object: WeatherService.Callback{
            override fun onSuccess(temp: String?) {
                runOnUiThread {
                    binding.resultTv.text =temp
                }

            }

            override fun onFail(e: Throwable?) {
                runOnUiThread {
                    Toast.makeText(this@IndexActivity,e?.message.orDefaultIfNullOrEmpty("Error"),Toast.LENGTH_SHORT).show()
                }
            }

        })
    }


    fun requestTemp(){
        val url = URL("http://www.weather.com.cn/adat/sk/101010100.html")
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            setConnectTimeout(10*1000)
            setReadTimeout(10*1000)
        }
        conn.connect()

        if(conn.responseCode == 200){
            val input = conn.inputStream

        }
    }

    fun onBtnClick(v: View) {
        when (v.id) {
            R.id.text_tab_btn -> {
                startActivity(Intent(this, TextTabActivity::class.java))
            }
            R.id.text_icon_tab_btn -> {
                startActivity(Intent(this, TextIconTabActivity::class.java))
            }
            R.id.custom_tab_btn -> {
                startActivity(Intent(this, CustomTabActivity::class.java))
            }
            R.id.mixed_tab_btn -> {
                startActivity(Intent(this, MixedTabActivity::class.java))
            }
            R.id.refresh_tab_btn -> {
                startActivity(Intent(this, DynamicTabActivity::class.java))
            }
        }
    }
}