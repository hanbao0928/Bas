package bas.leanback.compat.focussearch

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.leanback.widget.BaseGridView
import androidx.leanback.widget.VerticalGridView
import bas.leanback.compat.R
import bas.leanback.compat.logd
import bas.lib.core.lang.annotation.CallByOwner

/**
 * Created by Lucio on 2021/11/28.
 */
abstract class AbstractGridViewFocusSearchHelper<T : BaseGridView>(
    context: Context,
    val view: T,
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
            (Build.VERSION.SDK_INT >= 17) && (context.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL)
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.lbBaseGridView)
        focusOutFront = a.getBoolean(R.styleable.lbBaseGridView_focusOutFront, false)
        focusOutEnd = a.getBoolean(R.styleable.lbBaseGridView_focusOutEnd, false)
        focusOutSideStart =
            a.getBoolean(R.styleable.lbBaseGridView_focusOutSideStart, true)
        focusOutSideEnd = a.getBoolean(R.styleable.lbBaseGridView_focusOutSideEnd, true)
        a.recycle()
    }

    protected abstract val orientation: Int

    @bas.lib.core.lang.annotation.CallByOwner
    fun onRtlPropertiesChanged(layoutDirection: Int) {
        isRtl = layoutDirection == View.LAYOUT_DIRECTION_RTL
    }

    protected abstract fun getNextFocusAdapterPosition(
        currentAdapterPosition: Int,
        direction: Int
    ): Int

    @bas.lib.core.lang.annotation.CallByOwner
    fun focusSearch(focused: View?, direction: Int): View? {
        val next = callback.callSuperFocusSearch(focused, direction)
        if (view.isFocusSearchDisabled ||  next == null || (next != view && next != focused)) {
            //找到了合适的新的焦点view
            logd("[GridViewFocusSearchHelper] super.focusSearch success(focused=${focused},next=$next)")
            return next
        }
        if (next == view) {
            //该情况为 view的parent也没找到焦点，layoutManager便返回了baseGridView，该情况暂时不考虑
            return next
        }
        //else：找到的next与focused相同，该情况得优化寻焦逻辑
        val itemView = view.findContainingItemView(next)
        if (itemView == null) {
            logd("[GridViewFocusSearchHelper] custom:findContainingItemView == null,interrupt )")
            return next
        }

        val focusedAdapterPosition = view.getChildAdapterPosition(itemView)
        if (focusedAdapterPosition == NO_POSITION) {
            //no position 直接返回
            logd("[GridViewFocusSearchHelper] custom:focusedAdapterPosition == NO_POSITION,interrupt )")
            return next
        }

        logd("[GridViewFocusSearchHelper] custom:focusedAdapterPosition ==${focusedAdapterPosition},continue)")

        val nextFocusAdapterPosition =
            getNextFocusAdapterPosition(focusedAdapterPosition, direction)
        if (nextFocusAdapterPosition == focusedAdapterPosition || nextFocusAdapterPosition == NO_POSITION) {
            logd("[GridViewFocusSearchHelper] custom:nextFocusAdapterPosition == ${nextFocusAdapterPosition},interrupt )")
            return next
        }

        logd("[GridViewFocusSearchHelper] custom:nextFocusAdapterPosition ==${nextFocusAdapterPosition},continue)")

        val nextChildViewHolder = view.findViewHolderForAdapterPosition(nextFocusAdapterPosition)
        if (nextChildViewHolder == null) {
            logd("[GridViewFocusSearchHelper] custom:nextChildViewHolder == null,interrupt )")
            return next
        } else {
            return nextChildViewHolder.itemView
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
        return view.adapter?.itemCount ?: 0
    }

    companion object {
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
        ): AbstractGridViewFocusSearchHelper<VerticalGridView> {
            return VerticalGridViewFocusSearchHelper(context, view, callback, attrs, defStyleAttr)
        }
    }

    interface Callback {
        fun callSuperFocusSearch(focused: View?, direction: Int): View?
    }
}