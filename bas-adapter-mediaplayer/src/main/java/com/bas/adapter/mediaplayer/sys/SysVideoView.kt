package com.bas.adapter.mediaplayer.sys

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.widget.VideoView
import androidx.annotation.RequiresApi
import com.bas.adapter.mediaplayer.MediaPlayer
import com.bas.core.android.util.Logger
import java.util.concurrent.CopyOnWriteArrayList
import android.media.MediaPlayer as SysMediaPlayer

/**
 * Created by Lucio on 2021/4/21.
 */
class SysVideoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : VideoView(context, attrs, defStyleAttr), MediaPlayer {

    private var listeners: CopyOnWriteArrayList<MediaPlayer.PlayerListener> =
        CopyOnWriteArrayList<MediaPlayer.PlayerListener>()

    private var loadingAssistPosition: Int = -1
    private val loadingAssistRunnable = object : Runnable {
        override fun run() {
            val duration = currentPosition
            if (isPlaying && loadingAssistPosition == duration) {
                listeners.forEach {
                    it.onPlayBuffering()
                }
            } else {
                listeners.forEach {
                    it.onPlayBufferingEnd()
                }
            }
            loadingAssistPosition = duration
            postDelayed(this, 1000)
        }
    }

    private val internalPreparedListener = SysMediaPlayer.OnPreparedListener {
        log("OnPreparedListener")
        performLoadingAssist()
        listeners.forEach {
            it.onPlayPrepared()
        }
    }

    private val internalCompleteListener = SysMediaPlayer.OnCompletionListener {
        log("OnCompletionListener")
        cancelLoadingAssist()
        listeners.forEach {
            it.onPlayEnd()
        }
    }

    private val internalErrorListener = SysMediaPlayer.OnErrorListener { mediaPlayer, what, extra ->
        log("OnErrorListener(what=$what extra=$extra)")
        cancelLoadingAssist()
        listeners.forEach {
            it.onPlayError(SysMediaPlayerError.new(what, extra))
        }
        true
    }

    private val internalInfoListener = SysMediaPlayer.OnInfoListener { mp, what, extra ->
        log("OnInfoListener(what=$what extra=$extra)")
        when (what) {
            SysMediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                listeners.forEach {
                    it.onPlayBuffering()
                }
            }
            SysMediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                listeners.forEach {
                    it.onPlayBufferingEnd()
                }
            }

            SysMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> {
                listeners.forEach {
                    it.onPlayStart()
                }
            }
        }
        false
    }

    init {
        setOnCompletionListener(internalCompleteListener)
        setOnErrorListener(internalErrorListener)
        setOnPreparedListener(internalPreparedListener)
        setOnInfoListener(internalInfoListener)
    }

    private fun log(msg: String) {
        Logger.d("SysVideoView", msg)
    }

    /**
     * 执行加载状态辅助操作：某些盒子loading状态回调之后，进入播放状态，但是没有任何回调
     */
    private fun performLoadingAssist() {
        cancelLoadingAssist()
        post(loadingAssistRunnable)
    }

    fun cancelLoadingAssist() {
        removeCallbacks(loadingAssistRunnable)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = getDefaultSize(0, widthMeasureSpec)
        val height = getDefaultSize(0, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun setDataSource(url: String) {
        setVideoPath(url)
    }

    /**
     * 设置播放数据
     * @param seekTimeMs 开始播放时间，单位ms
     */
    override fun setDataSource(url: String, seekTimeMs: Int) {
        setVideoPath(url)
        seekTo(seekTimeMs)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun setDataSource(url: String, headers: Map<String, String>) {
        setVideoURI(Uri.parse(url), headers)
    }

    /**
     * 设置播放数据
     * @param headers 播放设置的headers
     * @param seekTimeMs 开始播放时间，单位ms
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun setDataSource(url: String, headers: Map<String, String>, seekTimeMs: Int) {
        setVideoURI(Uri.parse(url), headers)
        seekTo(seekTimeMs)
    }

    /**
     * 停止播放
     */
    override fun stop() {
        stopPlayback()
    }

    /**
     * 释放播放器
     */
    override fun release() {
        stopPlayback()
    }

    override fun addPlayerListener(listener: MediaPlayer.PlayerListener) {
        listeners.add(listener)
    }

    override fun removePlayerListener(listener: MediaPlayer.PlayerListener) {
        listeners.remove(listener)
    }

}