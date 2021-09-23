package com.bas.adapter.mediaplayer.qn

import android.content.Context
import android.util.AttributeSet
import com.bas.adapter.mediaplayer.BaseMediaPlayerView
import com.bas.adapter.mediaplayer.MediaPlayer
import com.pili.pldroid.player.*
import com.pili.pldroid.player.widget.PLVideoView
import java.util.concurrent.CopyOnWriteArrayList


/**
 * Created by Lucio on 2021/9/19.
 */
class QNVideoView : BaseMediaPlayerView {

    val kernel: PLVideoView

    private var defaultAVOptions: AVOptions = QNPlayer.VideoPlayOptions()

    private var listeners: CopyOnWriteArrayList<MediaPlayer.PlayerListener> =
        CopyOnWriteArrayList<MediaPlayer.PlayerListener>()

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        kernel = PLVideoView(context)
        addView(kernel, createDefaultKernelViewLayoutParams(context, attrs, defStyleAttr))
        initKernel()
    }

    private val internalPreparedListener = PLOnPreparedListener {
        listeners.forEach {
            it.onPlayPrepared()
        }
    }

    private val internalCompleteListener = PLOnCompletionListener {
        listeners.forEach {
            it.onPlayEnd()
        }
    }

    private val internalErrorListener = PLOnErrorListener { what ->
        //忽略拖动失败异常
        if (what == PLOnErrorListener.ERROR_CODE_SEEK_FAILED)
            return@PLOnErrorListener true
        listeners.forEach {
            it.onPlayError(QNPlayerError(what))
        }
        true
    }

    private val internalInfoListener = PLOnInfoListener { what, extra ->
        when (what) {
            PLOnInfoListener.MEDIA_INFO_BUFFERING_START -> {
                listeners.forEach {
                    it.onPlayBuffering()
                }
            }
            PLOnInfoListener.MEDIA_INFO_BUFFERING_END -> {
                listeners.forEach {
                    it.onPlayBufferingEnd()
                }
            }

            PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START -> {
                listeners.forEach {
                    it.onPlayStart()
                }
            }
        }
    }

    private fun initKernel() {
        kernel.setOnPreparedListener(internalPreparedListener)
        kernel.setOnCompletionListener(internalCompleteListener)
        kernel.setOnErrorListener(internalErrorListener)
        kernel.setOnInfoListener(internalInfoListener)
        setDefaultConfig()
    }

    fun setAVOptions(options: AVOptions) {
        defaultAVOptions = options
        kernel.setAVOptions(options)
    }

    private fun setDefaultConfig() {
        //默认铺满全屏幕
        kernel.displayAspectRatio = PLVideoView.ASPECT_RATIO_FIT_PARENT
    }

    /**
     * 设置播放数据
     */
    override fun setDataSource(url: String) {
        val options = defaultAVOptions
        options.setInteger(AVOptions.KEY_START_POSITION, 0)
        kernel.setAVOptions(options)
        kernel.setVideoPath(url)
    }

    /**
     * 设置播放数据
     * @param seekTimeMs 开始播放时间，单位ms
     */
    override fun setDataSource(url: String, seekTimeMs: Int) {
        val options = defaultAVOptions
        options.setInteger(AVOptions.KEY_START_POSITION, seekTimeMs)
        kernel.setAVOptions(options)
        kernel.setVideoPath(url)
    }

    override fun setDataSource(url: String, headers: Map<String, String>) {
        val options = defaultAVOptions
        options.setInteger(AVOptions.KEY_START_POSITION, 0)
        kernel.setAVOptions(options)
        kernel.setVideoPath(url, headers)
    }

    /**
     * 设置播放数据
     * @param headers 播放设置的headers
     * @param seekTimeMs 开始播放时间，单位ms
     */
    override fun setDataSource(url: String, headers: Map<String, String>, seekTimeMs: Int) {
        val options = defaultAVOptions
        options.setInteger(AVOptions.KEY_START_POSITION, seekTimeMs)
        kernel.setAVOptions(options)
        kernel.setVideoPath(url, headers)
    }

    /**
     * 是否正在播放
     */
    override fun isPlaying(): Boolean {
        return kernel.isPlaying
    }

    /**
     * 从指定位置开始播放
     * @param positionMs ms
     */
    override fun seekTo(positionMs: Int) {
        kernel.seekTo(positionMs.toLong())
    }

    /**
     * 获取持续时间
     * @return ms
     */
    override fun getDuration(): Int {
        return kernel.duration.toInt()
    }

    /**
     * 获取当前播放位置
     * @return ms
     */
    override fun getCurrentPosition(): Int {
        return kernel.currentPosition.toInt()
    }

    /**
     * 开始播放
     */
    override fun start() {
        kernel.start()
    }

    /**
     * 暂停播放
     */
    override fun pause() {
        kernel.pause()
    }


    /**
     * 停止播放
     */
    override fun stop() {
        kernel.stopPlayback()
    }

    /**
     * 释放播放器
     */
    override fun release() {
        kernel.stopPlayback()
    }

    /**
     * 缓冲百分比
     * @return ms
     */
    override fun getBufferPercentage(): Int {
        return kernel.bufferPercentage
    }

    override fun addPlayerListener(listener: MediaPlayer.PlayerListener) {
        listeners.add(listener)
    }

    override fun removePlayerListener(listener: MediaPlayer.PlayerListener) {
        listeners.remove(listener)
    }
}