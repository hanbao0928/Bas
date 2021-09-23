package com.bas.adapter.mediaplayer.tencent

import com.bas.adapter.mediaplayer.MediaPlayerError
import com.bas.core.android.basCtx
import com.bas.core.android.util.isNetworkConnected
import com.tencent.rtmp.TXLiveConstants

/**
 * Created by Lucio on 2021/9/19.
 */
class TXPlayerError(val errorCode: Int) :
    MediaPlayerError(Companion.getFriendlyMessage(errorCode, false)) {

    companion object {
        @JvmStatic
        fun new(errorCode: Int): TXPlayerError {
            return TXPlayerError(errorCode)
        }

        @JvmStatic
        private fun getFriendlyMessage(errorCode: Int, isLive: Boolean): String {
            return when (errorCode) {
                TXLiveConstants.PLAY_ERR_NET_DISCONNECT -> {
                    if (isLive) {
                        if (basCtx.isNetworkConnected()) {
                            "直播已断开"
                        } else {
                            "连接失败（网络错误或服务器已断开）"
                        }
                    } else {
                        "网络异常"
                    }
                }
                TXLiveConstants.PLAY_ERR_HLS_KEY -> {
                    "HLS 解密 key 获取失败"
                }
                else -> {
                    "未知错误:code=${errorCode}"
                }
            }
        }

    }

    override fun getFriendlyMessage(isLive: Boolean): String {
        return Companion.getFriendlyMessage(errorCode, isLive)
    }
}