package com.bas.android.leanback.flexbox

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.google.android.flexbox.FlexboxLayout

/**
 * Created by Lucio on 2021/10/31.
 */
class LeanbackFlexboxLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FlexboxLayout(context, attrs, defStyleAttr) {

    private val bringToFrontHelper: BringToFrontHelper = BringToFrontHelper(this)

    //是否处理[bringToFront]带来的问题：调用[bringToFront]的view总是在最后绘制的问题
    private var isBringToFrontEnabled = true

    //内部包含的Children请求焦点时，是否将当前布局bringToFront，避免被覆盖
    private var bringToFrontWhenChildRequestFocus = false

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.LeanbackFlexboxLayout)
        bringToFrontWhenChildRequestFocus = ta.getBoolean(
            R.styleable.LeanbackFlexboxLayout_bringToFrontWhenChildRequestFocus_blf,
            false
        )
        ta.recycle()
        //设置启用Children的绘制顺序：
        // 避免类似LinearLayout、FlexboxLayout等总是在布局末尾绘制调用bringToFront()的Child
        isChildrenDrawingOrderEnabled = true
    }

    /**
     * 是否处理Children调用[View.bringToFront]带来的问题：通过更改绘制顺序
     * 默认true
     */
    fun setChildBringToFrontCompatEnabled(isEnabled: Boolean) {
        isBringToFrontEnabled = isEnabled
    }

    /**
     * child view 请求焦点时，是否调用[bringToFront],避免当前Group被遮住
     */
    fun setBringToFrontWhenChildRequestFocus(isEnabled: Boolean) {
        bringToFrontWhenChildRequestFocus = isEnabled
    }

    override fun requestChildFocus(child: View?, focused: View?) {
        super.requestChildFocus(child, focused)
        if (!bringToFrontWhenChildRequestFocus || descendantFocusability == ViewGroup.FOCUS_BLOCK_DESCENDANTS) {
            return
        }
        if (indexOfChild(child) >= 0) {
            //为当前child view，bringToFront
            bringChildToFront(child)
        }
    }

    override fun bringChildToFront(child: View?) {
        bringToFrontHelper.bringChildToFront(child)
    }

    override fun getChildDrawingOrder(childCount: Int, drawingPosition: Int): Int {
        return if (isBringToFrontEnabled) {
            bringToFrontHelper.getChildDrawingOrder(childCount, drawingPosition)
        } else {
            super.getChildDrawingOrder(childCount, drawingPosition)
        }
    }

}