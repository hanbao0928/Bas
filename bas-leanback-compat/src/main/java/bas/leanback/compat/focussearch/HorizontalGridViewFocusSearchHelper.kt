package bas.leanback.compat.focussearch

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.leanback.widget.HorizontalGridView

/**
 * Created by Lucio on 2021/11/29.
 */
class HorizontalGridViewFocusSearchHelper(
    context: Context,
    view: HorizontalGridView,
    callback: Callback,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractGridViewFocusSearchHelper<HorizontalGridView>(
    context,
    view,
    callback,
    attrs,
    defStyleAttr
) {
    override val orientation: Int = HORIZONTAL

    override fun getNextFocusAdapterPosition(currentAdapterPosition: Int, direction: Int): Int {
        val maxCount = Math.max(0, getItemCount() - 1)
        when (direction) {
            View.FOCUS_LEFT -> {
                if (isRtl) {
                    return Math.min(maxCount, currentAdapterPosition + 1)
                } else {
                    return Math.max(0, currentAdapterPosition - 1)
                }
            }
            View.FOCUS_RIGHT -> {
                if (isRtl) {
                    return Math.max(0, currentAdapterPosition - 1)
                } else {
                    return Math.min(maxCount, currentAdapterPosition + 1)
                }
            }
            View.FOCUS_DOWN, View.FOCUS_UP -> {
                return currentAdapterPosition
            }
        }
        return currentAdapterPosition
    }
}