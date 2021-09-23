package com.bas.adapter.mediaplayer

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Created by Lucio on 2021/9/22.
 */
abstract class BaseMediaPlayerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), MediaPlayer {

    companion object {

        @JvmStatic
        fun createDefaultKernelViewLayoutParams(
            ctx: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0
        ): FrameLayout.LayoutParams {
            return FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }
    }
}