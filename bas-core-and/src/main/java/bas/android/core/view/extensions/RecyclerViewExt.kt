package com.bas.android.leanback.compat

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Lucio on 2021/11/29.
 */

const val NO_POSITION = RecyclerView.NO_POSITION

fun ViewGroup.findContainingItemView(view: View): View? {
    var result = view
    var parent = result.parent
    while (parent != null && parent !== this && parent is View) {
        result = parent
        parent = result.parent
    }
    return if (parent === this) result else null
}

fun RecyclerView.findImmediateChildIndex(view: View?): Int {
    if (view == null || view == this)
        return NO_POSITION

    val itemView = this.findContainingItemView(view)
    if (itemView != null) {
        var i = 0
        val count: Int = this.childCount
        while (i < count) {
            if (this.getChildAt(i) == view) {
                return i
            }
            i++
        }
    }
    return NO_POSITION
}

/**
 * 是否正在滑动中
 */
fun RecyclerView.isScrolling():Boolean{
    return scrollState != RecyclerView.SCROLL_STATE_IDLE
}