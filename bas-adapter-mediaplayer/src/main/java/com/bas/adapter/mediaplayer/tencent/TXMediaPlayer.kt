package com.bas.adapter.mediaplayer.tencent

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.bas.adapter.mediaplayer.BaseMediaPlayerView
import com.bas.adapter.mediaplayer.MediaPlayer
import com.bas.core.android.util.Logger
import com.tencent.rtmp.ITXVodPlayListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXVodPlayConfig
import com.tencent.rtmp.TXVodPlayer
import com.tencent.rtmp.ui.TXCloudVideoView
import java.util.concurrent.CopyOnWriteArrayList


/**
 * Created by Lucio on 2021/9/20.
 */
class TXMediaPlayer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseMediaPlayerView(context, attrs, defStyleAttr), MediaPlayer {

    companion object {

        private fun Long.toFloatTime(): Float {
            return this / 1000f
        }

        private fun Int.toFloatTime(): Float {
            return this / 1000f
        }
    }

    val kernelView: TXCloudVideoView
    val kernel: TXVodPlayer

    private var playUrl: String? = null

    private var listeners: CopyOnWriteArrayList<MediaPlayer.PlayerListener> =
        CopyOnWriteArrayList<MediaPlayer.PlayerListener>()

    private val internalListener = object : ITXVodPlayListener {
        override fun onPlayEvent(player: TXVodPlayer, event: Int, param: Bundle?) {
            Logger.i("TXMediaPlayer", "onPlayEvent(event=$event)")
            onKernelEvent(player, event, param)
        }

        override fun onNetStatus(player: TXVodPlayer, param: Bundle?) {
            Logger.i("TXMediaPlayer", "onNetStatus")
        }
    }

    init {
        kernelView = TXCloudVideoView(context)
        kernelView.visibility = View.GONE
        kernelView.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION)
        addView(kernelView, createDefaultKernelViewLayoutParams(context, attrs, defStyleAttr))
        kernel = TXVodPlayer(context)
        //默认开启硬解码，解码失败会切换成软解
        kernel.enableHardwareDecode(true)
        kernel.setPlayerView(kernelView)
        kernel.setVodListener(internalListener)
    }

    private fun onKernelEvent(player: TXVodPlayer, event: Int, param: Bundle?) {
        when (event) {
            TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED -> {
                listeners.forEach {
                    //播放器已准备完成，可以播放
                    it.onPlayPrepared()
                }
            }
            TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME -> {
                //网络接收到首个可渲染的视频数据包（IDR）
            }
            TXLiveConstants.PLAY_EVT_PLAY_PROGRESS -> {
//                //视频播放进度，会通知当前播放进度、加载进度 和总体时长
//                // 加载进度, 单位是毫秒
//                val duration_ms = param.getInt(TXLiveConstants.EVT_PLAYABLE_DURATION_MS)
//                mLoadBar.setProgress(duration_ms)
//
//                // 播放进度, 单位是毫秒
//                val progress_ms = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS_MS)
//                mSeekBar.setProgress(progress_ms)
//
//                // 视频总长, 单位是毫秒
//                val duration_ms = param.getInt(TXLiveConstants.EVT_PLAY_DURATION_MS)
//                // 可以用于设置时长显示等等
            }
            TXLiveConstants.PLAY_EVT_PLAY_BEGIN -> {
                //视频播放开始，如果有转菊花什么的这个时候该停了
                listeners.forEach {
                    it.onPlayStart()
                }
            }
            TXLiveConstants.PLAY_EVT_PLAY_LOADING -> {
                //视频播放 loading，如果能够恢复，之后会有 LOADING_END 事件
                listeners.forEach {
                    it.onPlayBuffering()
                }
            }
            TXLiveConstants.PLAY_EVT_VOD_LOADING_END -> {
                //视频播放 loading 结束，视频继续播放
                listeners.forEach {
                    it.onPlayBufferingEnd()
                }
            }
            TXLiveConstants.PLAY_EVT_PLAY_END -> {
                //视频播放结束
                listeners.forEach {
                    it.onPlayEnd()
                }
            }
            TXLiveConstants.PLAY_ERR_NET_DISCONNECT -> {
                //网络断连,且经多次重连亦不能恢复,更多重试请自行重启播放
                listeners.forEach {
                    it.onPlayError(TXPlayerError(event))
                }
            }
            TXLiveConstants.PLAY_ERR_HLS_KEY -> {
                //HLS 解密 key 获取失败
                listeners.forEach {
                    it.onPlayError(TXPlayerError(event))
                }
            }
        }
    }

    private fun createDefaultConfig(): TXVodPlayConfig {
        return TXVodPlayConfig()
    }

    /**
     * 设置播放数据
     */
    override fun setDataSource(url: String) {
        this.playUrl = url
        kernel.startPlay(url)
    }

    /**
     * 设置播放数据
     * @param seekTimeMs 开始播放时间，单位ms
     */
    override fun setDataSource(url: String, seekTimeMs: Int) {
        setStartTime(kernel, seekTimeMs)
        this.playUrl = url
        kernel.startPlay(url)
    }

    override fun setDataSource(url: String, headers: Map<String, String>) {
        val config = createDefaultConfig()
        config.setHeaders(headers)
        kernel.setConfig(config)
        this.playUrl = url
        kernel.startPlay(url)
    }

    /**
     * 设置播放数据
     * @param headers 播放设置的headers
     * @param seekTimeMs 开始播放时间，单位ms
     */
    override fun setDataSource(url: String, headers: Map<String, String>, seekTimeMs: Int) {
        val config = createDefaultConfig()
        config.setHeaders(headers)
        setStartTime(kernel, seekTimeMs)
        kernel.setConfig(config)
        this.playUrl = url
        kernel.startPlay(url)
    }

    private fun setStartTime(player: TXVodPlayer, startTime: Int) {
        if (startTime > 3000)
            player.setStartTime(startTime.toFloatTime())
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
        /*kernal.seek()
        跳转到视频流指定时间点. 可实现视频快进,快退,进度条跳转等功能.
        参数
        time	视频流时间点,单位为秒
        time	视频流时间点,小数点后为毫秒
        */
        kernel.seek(positionMs / 1000f)
    }

    /**
     * 获取持续时间
     * @return ms
     */
    override fun getDuration(): Int {
        //获取总时长 单位s
        return (kernel.duration * 1000).toInt()
    }

    /**
     * 获取当前播放位置
     * @return ms
     */
    override fun getCurrentPosition(): Int {
//     kernal.currentPlaybackTime   获取当前播放位置 单位秒
        return (kernel.currentPlaybackTime * 1000).toInt()
    }

    /**
     * 开始播放
     */
    override fun start() {
        kernel.resume()
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
        kernel.stopPlay(true)
    }

    /**
     * 释放播放器
     */
    override fun release() {
        kernel.stopPlay(true)
        kernelView.onDestroy()
    }

    /**
     * 缓冲百分比
     * @return ms
     */
    override fun getBufferPercentage(): Int {
        return (kernel.bufferDuration * 100f / kernel.duration).toInt()
    }

    override fun addPlayerListener(listener: MediaPlayer.PlayerListener) {
        listeners.add(listener)
    }

    override fun removePlayerListener(listener: MediaPlayer.PlayerListener) {
        listeners.remove(listener)
    }

    /**
     * 开启硬解码
     */
    fun enableHardwareDecode() {
        if (isPlaying() && !this.playUrl.isNullOrEmpty()) {
            val url = this.playUrl!!
            val position = this.getCurrentPosition()
            kernel.stopPlay(true)
            kernel.enableHardwareDecode(true)
            setDataSource(url, position)
        } else {
            kernel.stopPlay(true)
            kernel.enableHardwareDecode(true)
        }
    }
}