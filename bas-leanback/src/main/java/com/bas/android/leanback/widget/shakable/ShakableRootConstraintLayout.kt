package com.bas.android.leanback.widget.shakable

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bas.core.android.util.shakeX
import com.bas.core.android.util.shakeY

/**
 * Created by Lucio on 2021/10/8.
 * 支持child view边界抖动：最好作为[android.app.Activity]的根布局套一层即可不用做其他处理
 */
class ShakableRootConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    override fun focusSearch(focused: View?, direction: Int): View? {
        val nextFocus = super.focusSearch(focused, direction)
        if (nextFocus == null && focused != null) {
            when (direction) {
                View.FOCUS_LEFT, View.FOCUS_RIGHT -> {
                    focused.shakeX()
                }
                else -> {
                    focused.shakeY()
                }
            }
        }
        return nextFocus
    }

}