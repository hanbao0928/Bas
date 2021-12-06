package com.bas.adapter.mediaplayer.sys

import android.media.MediaPlayer
import bas.android.core.basCtx
import bas.android.core.util.isNetworkConnected
import com.bas.adapter.mediaplayer.MediaPlayerError
import com.bas.core.lang.orDefaultIfNullOrEmpty

/**
 * Created by Lucio on 2021/9/18.
 */
class SysPlayerError private constructor(val what: Int, val extra: Int) :
    MediaPlayerError("what=${what} extra=${extra}") {

    companion object {

        @JvmStatic
        fun new(what: Int, extra: Int): SysPlayerError {
            return SysPlayerError(what, extra)
        }
    }

    override fun getFriendlyMessage(isLive: Boolean): String {
        if (isLive) {
            return getLiveFriendlyMessage()
        } else {
            return getVodFriendlyMessage()
        }
    }

    private fun getLiveFriendlyMessage(): String {
        return when (what) {
            MediaPlayer.MEDIA_ERROR_IO -> {
                if (basCtx.isNetworkConnected()) {
                    "直播已断开"
                } else {
                    "连接失败（网络错误或服务器已断开）"
                }
            }
            else -> {
                message.orDefaultIfNullOrEmpty("未知异常")
            }
        }
    }

    private fun getVodFriendlyMessage(): String {
        return when (what) {
            MediaPlayer.MEDIA_ERROR_IO -> {
                "连接失败（网络错误或服务器已断开）"
            }
            else -> {
                message.orDefaultIfNullOrEmpty("未知异常")
            }
        }
    }
}