package com.bas.android.leanback.widget.scalable

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.bas.android.leanback.Leanback
import com.bas.android.leanback.R
import com.bas.android.leanback.widget.focus.FocusHighlightHandler
import com.bas.android.leanback.widget.focus.ScaleFocusHighlightHelper
import com.bas.android.leanback.widget.focus.ZoomFactor

/**
 * 获取焦点自动放大的[AppCompatTextView]
 */
@SuppressLint("CustomViewStyleable")
class ScalableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr){

    private var mFocusHelper: FocusHighlightHandler
    private var mEnableScale: Boolean = true
    private var mBringToFrontOnFocus: Boolean = false

    init {
        isFocusable = true
        isClickable = true
        isFocusableInTouchMode = Leanback.isLeanbackMode

        val tp = context.obtainStyledAttributes(attrs, R.styleable.ScalableView)
        tp.also {
            mEnableScale = it.getBoolean(R.styleable.ScalableView_enableScale, true)
            val zoomIndex = it.getInteger(
                R.styleable.ScalableView_zoomFactor,
                ZoomFactor.ZOOM_FACTOR_XSMALL
            )
            val useDimmer = it.getBoolean(R.styleable.ScalableView_enableDimmer, false)
            mFocusHelper = ScaleFocusHighlightHelper.createScaleFocusHandler(zoomIndex, useDimmer)
            mBringToFrontOnFocus =
                it.getBoolean(R.styleable.ScalableConstraintLayout_bringToFrontOnFocus, false)
        }
        tp.recycle()
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)

        if (mBringToFrontOnFocus && focused)
            bringToFront()
        if (mEnableScale) {
            mFocusHelper.onItemFocused(this, focused)
        }
    }
}