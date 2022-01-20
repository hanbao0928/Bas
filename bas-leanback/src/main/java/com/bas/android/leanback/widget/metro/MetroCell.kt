package com.bas.android.leanback.widget.metro

import androidx.gridlayout.widget.GridLayout


/**
 * @param columnIndex 所在列
 * @param rowIndex 所在行
 * @param columnSpan 横跨列数
 * @param rowSpan 横跨行数
 */
open class MetroCell<T>(
    val data: T?,
    val itemViewType: Int,
    val rowSpec: GridLayout.Spec,
    val columnSpec: GridLayout.Spec
) {


    companion object {
        val span1Spec = GridLayout.spec(GridLayout.UNDEFINED, 1, GridLayout.FILL)
        val span2Spec = GridLayout.spec(GridLayout.UNDEFINED, 2, GridLayout.FILL)
        val span3Spec = GridLayout.spec(GridLayout.UNDEFINED, 3, GridLayout.FILL)
    }
}

open class CellStyle<T>(
    val rowSpan:Int = 1,
    val columnSpan:Int = 1,
    val start:Int = GridLayout.UNDEFINED,
)