package com.bas.android.leanback.flexbox

import android.view.View
import android.view.ViewGroup

/**
 * Created by Lucio on 2021/10/31.
 *
 * 用于在[android.widget.LinearLayout]、[android.widget.RelativeLayout]等布局中,[android.view.View.bringToFront]调用问题
 */
internal class BringToFrontHelper(private val viewGroup: ViewGroup) {

    /**
     * 当前[View.bringToFront]所在的索引
     */
    private var frontChildIndex: Int = -1

    fun bringChildToFront(child: View?) {
        frontChildIndex = viewGroup.indexOfChild(child)
        if (frontChildIndex >= 0) {
            viewGroup.postInvalidate()
            //如果有问题，可以使用ViewGroup的bringChildToFront方法中提供的
//            viewGroup.requestLayout()
//            viewGroup.invalidate()
        }
    }

    /**
     * 获取绘制顺序
     * @param childCount
     * @param drawingPosition 当前绘制位置
     * @return 最终绘制位置
     */
    fun getChildDrawingOrder(childCount: Int, drawingPosition: Int): Int {
        if (frontChildIndex < 0)
            return drawingPosition
        if (drawingPosition == childCount - 1) {
            //修正措施，避免越界：应该不需要
            if (frontChildIndex > childCount - 1) {
                frontChildIndex = childCount - 1
            }
            return frontChildIndex
        }

        if (drawingPosition == frontChildIndex) {
            return childCount - 1
        }

        //修正措施，避免越界：应该不需要
        if (childCount <= drawingPosition) {
            return childCount - 1
        }
        return drawingPosition
    }

}