/**
 * Created by Lucio on 2021/9/27.
 */

@file:JvmName("Views")

package com.bas.core.android.util

import android.view.View
import android.view.ViewGroup

inline var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

/**
 * 从ViewGroup移除自身
 */
inline fun View?.removeSelfFromParent() {
    if (this == null)
        return
    (this.parent as? ViewGroup)?.removeView(this)
}