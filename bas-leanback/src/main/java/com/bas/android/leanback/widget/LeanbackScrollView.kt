package com.bas.android.leanback.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView

/**
 * Created by Lucio on 2021/10/26.
 */
class LeanbackScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.scrollViewStyle
) : ScrollView(context, attrs, defStyleAttr) {

    private val listeners = mutableListOf<OnScrollChangeListener>()

    /**
     * This is called in response to an internal scroll in this view (i.e., the
     * view scrolled its own contents). This is typically as a result of
     * [.scrollBy] or [.scrollTo] having been
     * called.
     *
     * @param l Current horizontal scroll origin.
     * @param t Current vertical scroll origin.
     * @param oldl Previous horizontal scroll origin.
     * @param oldt Previous vertical scroll origin.
     */
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        listeners.forEach {
            it.onScrollChange(this, l, t, oldl, oldt)
        }
    }

    fun addOnScrollChangeListener(listener: OnScrollChangeListener) {
        listeners.add(listener)
    }

    interface OnScrollChangeListener {
        /**
         * Called when the scroll position of a view changes.
         *
         * @param v The view whose scroll position has changed.
         * @param scrollX Current horizontal scroll origin.
         * @param scrollY Current vertical scroll origin.
         * @param oldScrollX Previous horizontal scroll origin.
         * @param oldScrollY Previous vertical scroll origin.
         */
        fun onScrollChange(v: View?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int)
    }

}