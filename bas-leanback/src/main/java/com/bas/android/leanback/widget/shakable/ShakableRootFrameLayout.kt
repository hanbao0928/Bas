package com.bas.android.leanback.widget.shakable

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.bas.android.leanback.Leanback
import com.bas.core.android.util.shakeX
import com.bas.core.android.util.shakeY

/**
 * Created by Lucio on 2021/10/8.
 * 支持child view边界抖动：最好作为[android.app.Activity]的根布局套一层即可不用做其他处理
 */
class ShakableRootFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var shakeAnimEnable = Leanback.isLeanbackMode

    /**
     * 设置启用边界抖动动画
     */
    fun setShakeAnimEnable(isEnable: Boolean) {
        this.shakeAnimEnable = isEnable
    }

//    override fun focusSearch(direction: Int): View {
//        if(!shakeAnimEnable){
//            return super.focusSearch(direction)
//        }
//        val newFocus = super.focusSearch(direction)
//        if (newFocus == null) {
//            when (direction) {
//                View.FOCUS_LEFT, View.FOCUS_RIGHT -> {
//                    this.shakeX()
//                }
//                else -> {
//                    this.shakeY()
//                }
//            }
//        }
//        return newFocus
//    }

    override fun focusSearch(focused: View?, direction: Int): View? {
        if (!shakeAnimEnable) {
            return super.focusSearch(focused, direction)
        }
        val newFocus = super.focusSearch(focused, direction)
        if (newFocus == null || newFocus == focused) {
            when (direction) {
                View.FOCUS_LEFT, View.FOCUS_RIGHT -> {
                    focused?.shakeX()
                }
                else -> {
                    focused?.shakeY()
                }
            }
        }
        return newFocus
    }

}