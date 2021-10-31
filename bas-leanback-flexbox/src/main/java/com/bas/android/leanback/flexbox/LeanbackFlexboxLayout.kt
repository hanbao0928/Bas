package com.bas.android.leanback.flexbox

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.flexbox.FlexboxLayout

/**
 * Created by Lucio on 2021/10/31.
 */
class LeanbackFlexboxLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FlexboxLayout(context, attrs, defStyleAttr) {

    private val bringToFrontHelper: BringToFrontHelper = BringToFrontHelper(this)
    private var isBringToFrontEnabled = true

    init {
        //设置启用Children的绘制顺序：
        // 避免类似LinearLayout、FlexboxLayout等总是在布局末尾绘制调用bringToFront()的Child
        isChildrenDrawingOrderEnabled = true
    }

    /**
     * 是否处理[bringToFront]带来的问题：通过更改绘制顺序
     */
    fun setBringToFrontCompatEnabled(isEnabled: Boolean) {
        isBringToFrontEnabled = isEnabled
    }

    override fun bringChildToFront(child: View?) {
        bringToFrontHelper.bringChildToFront(child)
    }

    /**
     * Converts drawing order position to container position. Override this
     * if you want to change the drawing order of children. By default, it
     * returns drawingPosition.
     *
     *
     * NOTE: In order for this method to be called, you must enable child ordering
     * first by calling [.setChildrenDrawingOrderEnabled].
     *
     * @param drawingPosition the drawing order position.
     * @return the container position of a child for this drawing order position.
     *
     * @see .setChildrenDrawingOrderEnabled
     * @see .isChildrenDrawingOrderEnabled
     */
    override fun getChildDrawingOrder(childCount: Int, drawingPosition: Int): Int {
        return if (isBringToFrontEnabled) {
            bringToFrontHelper.getChildDrawingOrder(childCount, drawingPosition)
        } else {
            super.getChildDrawingOrder(childCount, drawingPosition)
        }
    }

}