package com.bas.core.android.view.extensions

import android.view.View
import android.widget.TextView

/**
 * Created by Lucio on 2021/11/14.
 */
fun TextView.setTextOrGone(message: CharSequence?) {
    visibility = if (message.isNullOrEmpty()) {
        View.GONE
    } else {
        View.VISIBLE
    }
    text = message
}

fun TextView.setTextOrInvisible( message: CharSequence?) {
    visibility = if (message.isNullOrEmpty()) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
    text = message
}