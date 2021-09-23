package com.bas.adapter.mediaplayer

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.bas.adapter.mediaplayer.qn.QNVideoView
import com.bas.adapter.mediaplayer.sys.SysVideoView
import com.bas.adapter.mediaplayer.tencent.TXMediaPlayer
import com.bas.core.android.util.Logger
import com.bas.player.R

/**
 * Created by Lucio on 2021/9/22.
 */
class ConfigurablePlayerView private constructor(
     val kernel: MediaPlayer,
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseMediaPlayerView(context, attrs, defStyleAttr), MediaPlayer by kernel {

    @JvmOverloads
    constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : this(createKernelView(context, attrs, defStyleAttr), context, attrs, defStyleAttr)

    companion object {

        const val KERNEL_TYPE_SYS = 0

        const val KERNEL_TYPE_TX = 1

        const val KERNEL_TYPE_QN = 2

        @JvmStatic
        private fun createKernelView(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0
        ): MediaPlayer {
            val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.ConfigurablePlayerView,
                defStyleAttr,
                0
            )
            val type = a.getInt(R.styleable.ConfigurablePlayerView_kernel, KERNEL_TYPE_SYS)


            val kernelView = when (type) {
                KERNEL_TYPE_TX -> {
                    Logger.i("ConfigurablePlayerView","使用腾讯播放器内核")
                    TXMediaPlayer(context)
                }
                KERNEL_TYPE_QN -> {
                    Logger.i("ConfigurablePlayerView","使用七牛播放器内核")
                    QNVideoView(context)
                }
                else -> {
                    Logger.i("ConfigurablePlayerView","使用VideoView播放器内核")
                    SysVideoView(context)
                }
            }
            return kernelView
        }
    }

    init {
        val kernelView = kernel as View
        kernelView.id = R.id.kernel_view_id
        addView(kernelView, createDefaultKernelViewLayoutParams(context,attrs, defStyleAttr))
    }

    val kernelView: View? get() = findViewById(R.id.kernel_view_id)

}