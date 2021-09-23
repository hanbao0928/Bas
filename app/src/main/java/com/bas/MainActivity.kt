package com.bas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bas.core.android.util.getWifiSSID
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.tag("Hello").d("Hello World")
        this.getWifiSSID()
    }
}