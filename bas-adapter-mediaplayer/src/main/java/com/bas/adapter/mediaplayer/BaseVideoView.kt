package com.bas.adapter.mediaplayer

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Created by Lucio on 2021/9/22.
 */
abstract class BaseVideoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), MediaPlayer {

    companion object {

        /**
         * 生成默认内核视图布局参数
         */
        @JvmStatic
        internal fun generateDefaultKernelViewLayoutParams(
            ctx: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0
        ): LayoutParams {
            return LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        }
    }
}