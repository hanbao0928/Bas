package com.bas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import bas.droid.adapter.mediaplayer.ConfigurableVideoView
import bas.droid.adapter.mediaplayer.MediaPlayer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val url = ""
        findViewById<ConfigurableVideoView>(R.id.player).also {
            it.addPlayerListener(object: MediaPlayer.PlayerListener{
                override fun onPlayPrepared() {
                    super.onPlayPrepared()
                    it.start()
                }

                /**
                 * 播放结束
                 */
                override fun onPlayEnd() {
                    super.onPlayEnd()
                    Handler().postDelayed({
                        findViewById<ConfigurableVideoView>(R.id.player2).also {
                            it.addPlayerListener(object:MediaPlayer.PlayerListener{
                                override fun onPlayPrepared() {
                                    super.onPlayPrepared()
                                    it.start()
                                }
                            })
                            it.setDataSource(url)
                        }
                    },10000)
                }
            })

        }


    }
}