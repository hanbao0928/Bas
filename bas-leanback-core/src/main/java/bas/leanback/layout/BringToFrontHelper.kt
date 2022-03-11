package bas.leanback.layout

import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import bas.leanback.core.CallByOwner
import bas.leanback.core.R

/**
 * Created by Lucio on 2021/10/31.
 *
 * 用于处理[View.bringToFront]带来的问题：绘制顺序、bringToFront传递、
 *
 * 用于在[android.widget.LinearLayout]等布局中,[android.view.View.bringToFront]调用问题
 */
class BringToFrontHelper private constructor(
    private val layout: ViewGroup,
    private val callback: Callback,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) {

    /**
     * 当前[View.bringToFront]所在的索引
     */
    private var frontChildIndex: Int = NO_POSITION

    private val isChildrenDrawingOrderEnabled: Boolean get() = callback.isChildrenDrawingOrderEnabled()
    private val bringChildrenToFrontWhenRequestFocus: Boolean
    private val duplicateChildrenBringToFront: Boolean

    init {
        val ta = layout.context.obtainStyledAttributes(attrs, R.styleable.LeanbackLayout)
        bringChildrenToFrontWhenRequestFocus = ta.getBoolean(
            R.styleable.LeanbackLayout_lbl_bringChildToFrontWhenRequestFocus_bas,
            DEFAULT_BRING_CHILDREN_TO_FRONT
        )
        duplicateChildrenBringToFront = ta.getBoolean(
            R.styleable.LeanbackLayout_lbl_duplicateChildrenBringToFront_bas,
            DEFAULT_DUPLICATE_CHILDREN_BRING_TO_FRONT
        )
        ta.recycle()
    }

    @CallByOwner
    fun requestChildFocus(child: View?, focused: View?) {
        if (child == null
            || !bringChildrenToFrontWhenRequestFocus
            || layout.descendantFocusability == ViewGroup.FOCUS_BLOCK_DESCENDANTS
        ) {
            return
        }
        child.bringToFront()
    }

    @CallByOwner
    fun bringChildToFront(child: View?) {
        performChildrenBringToFront(child)
        performDuplicateBringToFront()
    }

    /**
     * 执行 children  bringToFront
     * */
    private fun performChildrenBringToFront(child: View?) {
        if (!isChildrenDrawingOrderEnabled) {
            callback.callSuperBringChildToFront(child)
            return
        }
        frontChildIndex = layout.indexOfChild(child)
        if (frontChildIndex >= 0) {
            layout.postInvalidate()
        }
    }

    /**
     * 当前layout是否同步执行bringToFront
     */
    private fun performDuplicateBringToFront() {
        if (!duplicateChildrenBringToFront)
            return
        layout.bringToFront()
    }

    /**
     * 获取绘制顺序
     * note:该方法被执行，说明layout是开启了自定义顺序的
     * @param childCount
     * @param drawingPosition 当前绘制位置
     * @return 最终绘制位置
     */
    @CallByOwner
    fun getChildDrawingOrder(childCount: Int, drawingPosition: Int): Int {
        if (frontChildIndex < 0) {
            //禁用、或数据不正确时，返回默认位置
            return drawingPosition
        }

        val maxIndex = childCount - 1
        if (frontChildIndex > maxIndex) {
            //修正措施，避免越界：应该不需要
            frontChildIndex = maxIndex
        }

        // supposely 0 1 2 3 4 5 6 7 8 9, 4 is the center item
        // drawing order is 0 1 2 3 9 8 7 6 5 4
        return if (drawingPosition < frontChildIndex) {
            drawingPosition
        } else if (drawingPosition < maxIndex) {
            frontChildIndex + maxIndex - drawingPosition
        } else {
            frontChildIndex
        }
//
//
//        if (drawingPosition == childCount - 1) {
//            //修正措施，避免越界：应该不需要
//            if (frontChildIndex > childCount - 1) {
//                frontChildIndex = childCount - 1
//            }
//            return drawingPosition
//        }
//
//        if (drawingPosition == frontChildIndex) {
//            //绘制本该绘制的位置时，先绘制最后一个
//            return childCount - 1
//        }
//
//        //修正措施，避免越界：应该不需要
//        if (childCount <= drawingPosition) {
//            return childCount - 1
//        }
//        return drawingPosition
    }

    interface Callback {
        /**
         * 是否修改Children绘制循序
         */
        fun isChildrenDrawingOrderEnabled(): Boolean

        fun callSuperBringChildToFront(child: View?)

        fun callSuperGetChildDrawingOrder(childCount: Int, drawingPosition: Int): Int
    }

    companion object {

        private const val NO_POSITION = -1

        private const val DEFAULT_BRING_CHILDREN_TO_FRONT = false

        private const val DEFAULT_DUPLICATE_CHILDREN_BRING_TO_FRONT = false

        /**
         * 布局是否支持Children 调用[View.bringToFront]
         */
        @JvmStatic
        fun isLayoutSupportBringChildToFront(viewGroup: ViewGroup): Boolean {
            /*也就是说children调用bingToFront并不会有什么问题*/
            return viewGroup is FrameLayout
                    || viewGroup is RelativeLayout
                    || viewGroup is ConstraintLayout
        }

        @JvmStatic
        fun create(
            layout: ViewGroup,
            callback: Callback,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0
        ): BringToFrontHelper {
            return BringToFrontHelper(layout, callback, attrs, defStyleAttr)
        }

    }
}