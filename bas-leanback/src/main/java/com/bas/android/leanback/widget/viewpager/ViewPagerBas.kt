package com.bas.android.leanback.widget.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import androidx.viewpager.widget.ViewPager

/**
 * Created by Lucio on 2021/10/8.
 */
class ViewPagerBas : ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var isKeyEventPageEnable = true

    /**
     * 设置是否响应按键进行翻页动作
     */
    fun setKeyEventPageEnable(isEnable: Boolean) {
        this.isKeyEventPageEnable = isEnable
    }

    /**
     * You can call this function yourself to have the scroll view perform
     * scrolling from a key event, just as if the event had been dispatched to
     * it by the view hierarchy.
     *
     * @param event The key event to execute.
     * @return Return true if the event was handled, else false.
     */
    override fun executeKeyEvent(event: KeyEvent): Boolean {
        return this.isKeyEventPageEnable && super.executeKeyEvent(event)
    }
}