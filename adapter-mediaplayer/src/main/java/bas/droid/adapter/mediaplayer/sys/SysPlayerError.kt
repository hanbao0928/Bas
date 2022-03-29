package bas.droid.adapter.mediaplayer.sys

import android.content.Context
import android.media.MediaPlayer
import bas.droid.adapter.mediaplayer.MediaPlayerError
import bas.droid.adapter.mediaplayer.isNetworkConnected

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

        @JvmStatic
        fun getFriendlyMessage(context: Context, what: Int, extra: Int, isLive: Boolean): String {
            if (isLive) {
                return getLiveFriendlyMessage(context, what, extra)
            } else {
                return getVodFriendlyMessage(context, what, extra)
            }
        }

        @JvmStatic
        private fun getLiveFriendlyMessage(context: Context, what: Int, extra: Int): String {
            return when (what) {
                MediaPlayer.MEDIA_ERROR_IO -> {
                    if (context.isNetworkConnected) {
                        "直播已断开"
                    } else {
                        "连接失败（网络错误或服务器已断开）"
                    }
                }
                else -> {
                    "what=${what} extra=${extra}"
                }
            }
        }

        private fun getVodFriendlyMessage(context: Context, what: Int, extra: Int): String {
            return when (what) {
                MediaPlayer.MEDIA_ERROR_IO -> {
                    "连接失败（网络错误或服务器已断开）"
                }
                else -> {
                    "what=${what} extra=${extra}"
                }
            }
        }
    }


}