package com.bas.sample.effectv2

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bas.R
import com.bas.databinding.ActivityEffectV2TestBinding
import kotlin.random.Random

class Effectv2Activity : AppCompatActivity(){

    lateinit var  binding : ActivityEffectV2TestBinding

    val radius = floatArrayOf(20f,35f,40f)
    val xco = floatArrayOf(0f,20f,10f)

    val colors = intArrayOf(Color.GREEN,Color.GRAY,Color.CYAN)

    val strokes = floatArrayOf(10f,20f,30f)

    private val randomIndex get() = Random.nextInt(0,3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEffectV2TestBinding.inflate(this.layoutInflater)
        setContentView(binding.root)
        binding.noshadow.setOnClickListener {
            binding.effectLayout.setDisableShadow()
        }
        binding.nostroke.setOnClickListener {
            binding.effectLayout.setDisableStroke()
        }
        binding.shadow.setOnClickListener {
            binding.effectLayout.setShadowLayer(radius[randomIndex],xco[randomIndex],xco[randomIndex],colors[randomIndex])
        }
        binding.stroke.setOnClickListener {
            binding.effectLayout.setStroke(strokes[randomIndex])
        }
        binding.all.setOnClickListener {
            binding.effectLayout.setShadowLayer(radius[randomIndex],xco[randomIndex],xco[randomIndex],colors[randomIndex])
            binding.effectLayout.setStroke(strokes[randomIndex])
        }
    }
}