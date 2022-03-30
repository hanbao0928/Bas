package bas.leanback.support.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.leanback.widget.BaseGridView
import androidx.leanback.widget.VerticalGridView

import bas.leanback.core.R

/**
 * Created by Lucio on 2021/11/28.
 * [BaseGridView]焦点查询帮助类
 *
 * @param gridView
 * @param callback 查询回调，用于判断[BaseGridView]是否已找到下一个焦点位置
 */

abstract class GridViewFocusSearchHelper<T : BaseGridView>(
    val gridView: T,
    val callback: Callback,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) {

    protected var isRtl: Boolean
    protected val focusOutFront: Boolean
    protected val focusOutEnd: Boolean

    //是否允许焦点转移
    protected val focusOutSideStart: Boolean
    protected val focusOutSideEnd: Boolean
    private val focusedRect = Rect()

    init {
        isRtl =
            (Build.VERSION.SDK_INT >= 17) && (gridView.context.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL)
        val a: TypedArray =
            gridView.context.obtainStyledAttributes(attrs, R.styleable.lbBaseGridView)
        focusOutFront = a.getBoolean(R.styleable.lbBaseGridView_focusOutFront, false)
        focusOutEnd = a.getBoolean(R.styleable.lbBaseGridView_focusOutEnd, false)
        focusOutSideStart =
            a.getBoolean(R.styleable.lbBaseGridView_focusOutSideStart, true)
        focusOutSideEnd = a.getBoolean(R.styleable.lbBaseGridView_focusOutSideEnd, true)
        a.recycle()
    }

    protected abstract val orientation: Int

    protected abstract fun getNextFocusAdapterPosition(
        currentAdapterPosition: Int,
        direction: Int
    ): Int

    @bas.lib.core.lang.annotation.CallByOwner
    fun onRtlPropertiesChanged(layoutDirection: Int) {
        isRtl = layoutDirection == View.LAYOUT_DIRECTION_RTL
    }

    @bas.lib.core.lang.annotation.CallByOwner
    fun focusSearch(focused: View?, direction: Int): View? {
        val next = callback.callSuperFocusSearch(focused, direction)
        if (gridView.isFocusSearchDisabled || next == null || (next != gridView && next != focused)) {
            //找到了合适的新的焦点view
            logd("super.focusSearch success(focused=${focused},next=$next)")
            return next
        }
        if (next == gridView) {
            //该情况为 view的parent也没找到焦点，layoutManager便返回了baseGridView，该情况暂时不考虑
            return next
        }
        //else：找到的next与focused相同，该情况得优化寻焦逻辑
        val itemView = gridView.findContainingItemView(next)
        if (itemView == null) {
            logd("custom:findContainingItemView == null,interrupt )")
            return next
        }

        val focusedAdapterPosition = gridView.getChildAdapterPosition(itemView)
        if (focusedAdapterPosition == NO_POSITION) {
            //no position 直接返回
            logd("custom:focusedAdapterPosition == NO_POSITION,interrupt )")
            return next
        }

        logd("custom:focusedAdapterPosition ==${focusedAdapterPosition},continue)")

        val nextFocusAdapterPosition =
            getNextFocusAdapterPosition(focusedAdapterPosition, direction)
        if (nextFocusAdapterPosition == focusedAdapterPosition || nextFocusAdapterPosition == NO_POSITION) {
            logd("custom:nextFocusAdapterPosition == ${nextFocusAdapterPosition},interrupt )")
            return next
        }

        logd("custom:nextFocusAdapterPosition ==${nextFocusAdapterPosition},continue)")

        val nextChildViewHolder =
            gridView.findViewHolderForAdapterPosition(nextFocusAdapterPosition)
        return if (nextChildViewHolder == null) {
            logd("custom:nextChildViewHolder == null,interrupt )")
            next
        } else {
            nextChildViewHolder.itemView
        }

    }

//    private fun hasCreatedLastItem(): Boolean {
//        val count: Int = getItemCount()
//        return count == 0 || view.findViewHolderForAdapterPosition(count - 1) != null
//    }
//
//    private fun hasCreatedFirstItem(): Boolean {
//        val count: Int = getItemCount()
//        return count == 0 || view.findViewHolderForAdapterPosition(0) != null
//    }

    protected fun getItemCount(): Int {
        return gridView.adapter?.itemCount ?: 0
    }

    private inline fun logd(msg: String) {
        Log.d(TAG, msg)
    }

    interface Callback {
        fun callSuperFocusSearch(focused: View?, direction: Int): View?
    }

    companion object {

        private const val TAG = "GVFocusSearchHelper"

        private const val PREV_ITEM = 0
        private const val NEXT_ITEM = 1
        private const val PREV_ROW = 2
        private const val NEXT_ROW = 3

        internal const val NO_POSITION = BaseGridView.NO_POSITION

        internal const val HORIZONTAL = BaseGridView.HORIZONTAL

        internal const val VERTICAL = BaseGridView.VERTICAL

        @JvmStatic
        fun create(
            context: Context,
            view: VerticalGridView,
            callback: Callback,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0
        ): GridViewFocusSearchHelper<VerticalGridView> {
            return VerticalGridViewFocusSearchHelper(view, callback, attrs, defStyleAttr)
        }
    }
}