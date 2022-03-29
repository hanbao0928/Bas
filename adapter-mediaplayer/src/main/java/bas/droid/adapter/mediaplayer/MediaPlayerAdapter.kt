package bas.droid.adapter.mediaplayer

import android.content.Context
import bas.droid.adapter.mediaplayer.qn.QNVideoView
import bas.droid.adapter.mediaplayer.sys.SysVideoView
import bas.droid.adapter.mediaplayer.tencent.TXVideoView

/**
 * Created by Lucio on 2021/9/27.
 */
object MediaPlayerAdapter {

    /**
     * 是否运行在tv之上
     * tv上运行要考虑焦点，要解决系统VideoView、七牛播放器默认获取焦点的问题
     */
    @JvmStatic
    var isLeanbackMode: Boolean = true

    @JvmStatic
    fun createVideoView(
        context: Context,
        @KernelType type: Int
    ): AbstractVideoView {
        val kernelView = when (type) {
            KernelType.TX -> {
                logi("使用腾讯播放器内核")
                TXVideoView(context)
            }
            KernelType.QN -> {
                logi("使用七牛播放器内核")
                QNVideoView(context)
            }
            KernelType.SYS -> {
                logi("使用VideoView播放器内核")
                SysVideoView(context)
            }
            else -> {
                throw IllegalStateException("创建播放器失败：type(=${type}) is invalidate.")
            }
        }
        return kernelView
    }
}