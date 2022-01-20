package bas.leanback.compat

import android.annotation.SuppressLint
import androidx.leanback.widget.BaseGridView

/**
 * Created by Lucio on 2021/12/21.
 */

/**
 * 使用居中对其（选中的item在中间）
 */
@SuppressLint("RestrictedApi")
fun BaseGridView.applyCenterAlignment(){
    focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ALIGNED
    windowAlignment = BaseGridView.WINDOW_ALIGN_NO_EDGE
}