package com.bas.adapter.mediaplayer

/**
 * Created by Lucio on 2021/4/15.
 */
interface MediaPlayer {

    /**
     * 设置播放数据
     */
    fun setDataSource(url: String)

    /**
     * 设置播放数据
     * @param seekTimeMs 开始播放时间，单位ms
     */
    fun setDataSource(url: String,seekTimeMs:Int)


    fun setDataSource(url: String, headers: Map<String, String>)

    /**
     * 设置播放数据
     * @param headers 播放设置的headers
     * @param seekTimeMs 开始播放时间，单位ms
     */
    fun setDataSource(url: String, headers: Map<String, String>,seekTimeMs:Int)

    /**
     * 是否正在播放
     */
    fun isPlaying(): Boolean

    /**
     * 从指定位置开始播放
     * @param positionMs ms
     */
    fun seekTo(positionMs: Int)

    /**
     * 获取持续时间
     * @return ms
     */
    fun getDuration(): Int

    /**
     * 获取当前播放位置
     * @return ms
     */
    fun getCurrentPosition(): Int

    /**
     * 开始播放
     */
    fun start()

    /**
     * 暂停播放
     */
    fun pause()

    /**
     * 停止播放
     */
    fun stop()

    /**
     * 释放播放器
     */
    fun release()

    /**
     * 缓冲百分比
     * @return ms
     */
    fun getBufferPercentage(): Int

    fun addPlayerListener(listener: PlayerListener)

    fun removePlayerListener(listener: PlayerListener)

//    fun addCompletionListener(listener: OnAMPlayerCompleteListener)
//    fun removeCompletionListener(listener: OnAMPlayerCompleteListener)
//
//    fun addErrorListener(listener: OnAMPlayerErrorListener)
//    fun removeErrorListener(listener: OnAMPlayerErrorListener)
//
//    fun addPreparedListener(listener: OnAMPlayerPreparedListener)
//    fun removePreparedListener(listener: OnAMPlayerPreparedListener)
//
//    fun addInfoListener(listener: OnAMPlayerInfoListener)
//    fun removeInfoListener(listener: OnAMPlayerInfoListener)
//

//    /**
//     * 播放已准备回调
//     */
//    fun interface OnAMPlayerPreparedListener {
//        fun onAMPlayerPrepared(player: MediaPlayer)
//    }
//
//    /**
//     * 错误结束回调
//     */
//    fun interface OnAMPlayerErrorListener {
//        /**
//         * @return 如果返回true则表示处理消耗了该异常，后续不会回调[OnAMPlayerCompleteListener],相反返回false则会触发后续回调
//         */
//        fun onAMPlayerError(player: MediaPlayer, e: Exception): Boolean
//    }

//    /**
//     * 播放结束回调
//     */
//    fun interface OnAMPlayerCompleteListener {
//        fun onAMPlayerComplete(player: MediaPlayer)
//    }
//
//    /**
//     * 信息回调
//     */
//    fun interface OnAMPlayerInfoListener {
//        fun onAMPlayerInfo(player: MediaPlayer, what: Int, extra: Int)
//    }
//

    /**
     * 播放器监听回调
     */
    interface PlayerListener {

        fun onPlayPrepared() {

        }

        /**
         * 开始缓冲
         */
        fun onPlayBuffering() {

        }

        /**
         * 停止缓冲
         */
        fun onPlayBufferingEnd() {

        }

        /**
         *  开始播放
         */
        fun onPlayStart() {

        }

        /**
         * 播放结束
         */
        fun onPlayEnd() {

        }

        /**
         * 播放出错
         */
        fun onPlayError(extra: Any?) {

        }
    }

//    interface Control {
//
//        /**
//         * 显示控制器
//         * @param timeout 超时时间，即超过多少时间之后隐藏
//         */
//        fun show(timeout: Long)
//
//        fun hide()
//
//        /**
//         * 设置播放器
//         */
//        fun setPlayer(player: MediaPlayer?)
//
//
//        /**
//         * Adds a [VisibilityListener].
//         *
//         * @param listener The listener to be notified about visibility changes.
//         */
//        fun addVisibilityListener(listener: VisibilityListener)
//
//        /**
//         * Removes a [VisibilityListener].
//         *
//         * @param listener The listener to be removed.
//         */
//        fun removeVisibilityListener(listener: VisibilityListener)
//
//        /** Listener to be notified about changes of the visibility of the UI control.  */
//        fun interface VisibilityListener {
//            /**
//             * Called when the visibility changes.
//             *
//             * @param visibility The new visibility. Either [View.VISIBLE] or [View.GONE].
//             */
//            fun onVisibilityChange(visibility: Int)
//        }
//    }

}