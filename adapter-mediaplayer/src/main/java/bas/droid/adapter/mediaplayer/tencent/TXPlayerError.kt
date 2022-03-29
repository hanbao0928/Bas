package bas.droid.adapter.mediaplayer.tencent

import android.content.Context
import bas.droid.adapter.mediaplayer.MediaPlayerError
import bas.droid.adapter.mediaplayer.isNetworkConnected
import com.tencent.rtmp.TXLiveConstants

/**
 * Created by Lucio on 2021/9/19.
 */
class TXPlayerError(val errorCode: Int, message: String) :
    MediaPlayerError(message) {

    companion object {

        @JvmStatic
        fun new(context: Context, errorCode: Int, isLive: Boolean = false): TXPlayerError {
            return TXPlayerError(errorCode, getFriendlyMessage(context,errorCode,isLive))
        }

        @JvmStatic
        fun new(errorCode: Int, message: String): TXPlayerError {
            return TXPlayerError(errorCode, message)
        }

        @JvmStatic
        fun getFriendlyMessage(context: Context, errorCode: Int, isLive: Boolean): String {
            return when (errorCode) {
                //https://cloud.tencent.com/document/product/454/17246
                TXLiveConstants.PLAY_ERR_NET_DISCONNECT -> {
                    if (isLive) {
                        if (context.isNetworkConnected) {
                            "直播已断开"
                        } else {
                            "连接失败（网络错误或服务器已断开）"
                        }
                    } else {
                        "网络异常"
                    }
                }

                TXLiveConstants.PLAY_ERR_FILE_NOT_FOUND -> {
                    "播放文件不存在"
                }
                TXLiveConstants.PLAY_ERR_HLS_KEY -> {
                    "HLS 解密 key 获取失败"
                }
                TXLiveConstants.PLAY_ERR_HEVC_DECODE_FAIL -> {
                    "H265 解码失败"
                }
                TXLiveConstants.PLAY_ERR_GET_PLAYINFO_FAIL -> {
                    "获取点播文件信息失败"
                }
                else -> {
                    "未知错误:code=${errorCode}"
                }
            }
        }

    }

}