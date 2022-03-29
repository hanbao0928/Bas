package bas.droid.adapter.mediaplayer.qn

import android.content.Context
import bas.droid.adapter.mediaplayer.MediaPlayerError
import bas.droid.adapter.mediaplayer.isNetworkConnected
import com.pili.pldroid.player.PLOnErrorListener

/**
 * Created by Lucio on 2021/9/19.
 */
class QNPlayerError private constructor(val errorCode: Int, message: String) :
    MediaPlayerError(message) {

    companion object {

        @JvmStatic
        fun new(context: Context, errorCode: Int, isLive: Boolean = false): QNPlayerError {
            return QNPlayerError(errorCode, getFriendlyMessage(context, errorCode, isLive))
        }

        @JvmStatic
        fun new(
            errorCode: Int,
            message: String
        ): QNPlayerError {
            return QNPlayerError(errorCode, message)
        }

        @JvmStatic
        internal fun getFriendlyMessage(
            context: Context,
            errorCode: Int,
            isLive: Boolean
        ): String {
            return when (errorCode) {
                PLOnErrorListener.ERROR_CODE_OPEN_FAILED -> {
                    "播放器打开失败"
                }
                PLOnErrorListener.ERROR_CODE_IO_ERROR -> {
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

}