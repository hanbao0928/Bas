package bas.leanback.support.widget

import android.util.AttributeSet
import android.view.View
import androidx.leanback.widget.VerticalGridView
import kotlin.math.max
import kotlin.math.min

/**
 * Created by Lucio on 2021/11/29.
 * 主要用于多列情况换行寻焦支持
 */
class VerticalGridViewFocusSearchHelper(
    view: VerticalGridView,
    callback: Callback,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : GridViewFocusSearchHelper<VerticalGridView>(
    view,
    callback,
    attrs,
    defStyleAttr
) {

    override val orientation: Int = VERTICAL

    /**
     * 获取用于下一个获取焦点的位置
     */
    override fun getNextFocusAdapterPosition(currentAdapterPosition: Int, direction: Int): Int {
        val maxCount = max(0, getItemCount() - 1)
        when (direction) {
            View.FOCUS_LEFT -> {
                return if (isRtl) {
                    min(maxCount, currentAdapterPosition + 1)
                } else {
                    max(0, currentAdapterPosition - 1)
                }
            }
            View.FOCUS_RIGHT -> {
                return if (isRtl) {
                    max(0, currentAdapterPosition - 1)
                } else {
                    min(maxCount, currentAdapterPosition + 1)
                }
            }
            View.FOCUS_DOWN, View.FOCUS_UP -> {
                return currentAdapterPosition
            }
        }
        return currentAdapterPosition
    }


}