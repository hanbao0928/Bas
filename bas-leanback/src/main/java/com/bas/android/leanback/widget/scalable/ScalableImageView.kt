package com.bas.android.leanback.widget.scalable

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.bas.android.leanback.Leanback
import com.bas.android.leanback.R
import com.bas.android.leanback.widget.focus.FocusHighlightHandler
import com.bas.android.leanback.widget.focus.ScaleFocusHighlightHelper
import com.bas.android.leanback.widget.focus.ZoomFactor

/**
 * 获取焦点自动放大的[AppCompatImageView]
 */
@SuppressLint("CustomViewStyleable")
class ScalableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr){

    private var mFocusHandler: FocusHighlightHandler
    private var mEnableScale: Boolean = true
    private var mBringToFrontOnFocus:Boolean = false
    private var mCustomFocusChangedListener : OnFocusChangeListener? = null
    private val mFocusChangedListener = OnFocusChangeListener(::onFocusChanged)

    init {
        isClickable = true
        isFocusable = Leanback.isLeanbackMode
        isFocusableInTouchMode = Leanback.isLeanbackMode
        onFocusChangeListener = mFocusChangedListener
        val tp = context.obtainStyledAttributes(attrs, R.styleable.ScalableView)
        tp.also {
            mEnableScale = it.getBoolean(R.styleable.ScalableView_enableScale, true)
            val zoomIndex = it.getInteger(
                R.styleable.ScalableView_zoomFactor,
                ZoomFactor.ZOOM_FACTOR_XSMALL
            )
            val useDimmer = it.getBoolean(R.styleable.ScalableView_enableDimmer, false)
            mFocusHandler = ScaleFocusHighlightHelper.createScaleFocusHandler(zoomIndex, useDimmer)
            mBringToFrontOnFocus =
                it.getBoolean(R.styleable.ScalableConstraintLayout_bringToFrontOnFocus, false)
        }
        tp.recycle()
        
        requestFocus()
    }

    private fun onFocusChanged(v:View,hasFocus:Boolean){
        if(mBringToFrontOnFocus && hasFocus)
            bringToFront()

        if (mEnableScale) {
            mFocusHandler.onItemFocused(v, hasFocus)
        }
        mCustomFocusChangedListener?.onFocusChange(v,hasFocus)
    }

    /**
     * 设置自定义焦点改变监听：不能通过重写[setOnFocusChangeListener]方法实现，否则在[androidx.leanback.widget.ItemBridgeAdapter]中作为根布局使用时会产生循环调用问题，导致anr直到溢出
     */
     fun setOnCustomFocusChangeListener(l: OnFocusChangeListener?) {
        mCustomFocusChangedListener = l
    }

}