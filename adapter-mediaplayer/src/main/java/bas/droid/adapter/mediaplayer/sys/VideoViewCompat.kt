package bas.droid.adapter.mediaplayer.sys

import android.content.Context
import android.util.AttributeSet
import android.widget.VideoView

/**
 * Created by Lucio on 2021/9/29.
 */
class VideoViewCompat @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : VideoView(context, attrs, defStyleAttr) {

    /**
     * 解决某些视频播放，视频没有铺满view 尺寸的问题
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = getDefaultSize(0, widthMeasureSpec)
        val height = getDefaultSize(0, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }
}