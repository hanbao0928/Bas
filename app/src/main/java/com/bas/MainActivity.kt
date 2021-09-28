package com.bas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.bas.adapter.mediaplayer.ConfigurableVideoView
import com.bas.adapter.mediaplayer.MediaPlayer
import com.pili.pldroid.player.widget.PLVideoView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "http://uxclass.ucuxin.com/uxdev/209/video/21/9/23/2653a0936b7f47a0a2c4c9b389c8e17f?Expires=1632721545&OSSAccessKeyId=LTAI8bWGyBmKUcM8&Signature=iWxkbq4YKS1vkVV%2B2lOJdijYjko%3D"
        findViewById<ConfigurableVideoView>(R.id.player).also {
            it.addPlayerListener(object:MediaPlayer.PlayerListener{
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

            it.setDataSource("http://highlight-video.cdn.bcebos.com/video/6s/790fb256-1db3-11ec-8b54-6c92bf3b6b4d.mp4?v_from_s=hkapp-haokan-tucheng")
        }


    }
}