package com.bas.adapter.mediaplayer.qn

import bas.droid.core.ctxBas
import bas.droid.core.util.isNetworkConnected
import com.bas.adapter.mediaplayer.MediaPlayerError
import com.pili.pldroid.player.PLOnErrorListener

/**
 * Created by Lucio on 2021/9/19.
 */
class QNPlayerError(val errorCode: Int) :
    MediaPlayerError(Companion.getFriendlyMessage(errorCode, false)) {

    companion object {
        @JvmStatic
        fun new(errorCode: Int): QNPlayerError {
            return QNPlayerError(errorCode)
        }

        @JvmStatic
        private fun getFriendlyMessage(errorCode: Int, isLive: Boolean): String {
            return when (errorCode) {
                PLOnErrorListener.ERROR_CODE_OPEN_FAILED -> {
                    "播放器打开失败"
                }
                PLOnErrorListener.ERROR_CODE_IO_ERROR -> {
                    if (isLive) {
                        if (ctxBas.isNetworkConnected()) {
                            "直播已断开"
                        } else {
                            "连接失败（网络错误或服务器已断开）"
                        }
                    } else {
                        "网络异常"
                    }
                }
                PLOnErrorListener.ERROR_CODE_CACHE_FAILED -> {
                    "预加载失败"
                }
                PLOnErrorListener.ERROR_CODE_HW_DECODE_FAILURE -> {
                    "硬解失败"
                }
                PLOnErrorListener.ERROR_CODE_PLAYER_DESTROYED -> {
                    "播放器已被销毁，需要再次 setVideoURL 或 prepareAsync"
                }
                PLOnErrorListener.ERROR_CODE_PLAYER_VERSION_NOT_MATCH -> {
                    "so 库版本不匹配，请升级APK"
                }
                PLOnErrorListener.ERROR_CODE_PLAYER_CREATE_AUDIO_FAILED -> {
                    "AudioTrack 初始化失败，可能无法播放音频"
                }
                else -> {
                    "未知错误"
                }
            }
        }

    }

    override fun getFriendlyMessage(isLive: Boolean): String {
        return Companion.getFriendlyMessage(errorCode, isLive)
    }


}