package bas.leanback.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnFocusChangeListener
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import bas.leanback.effectlayout.R

/**
 * Created by Lucio on 2022/2/8.
 */
open class LeanbackRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.recyclerViewStyle
) : RecyclerView(context, attrs, defStyleAttr) {

    fun interface OnItemClickListener {
        fun onItemClick(parent: LeanbackRecyclerView, itemView: View, position: Int)
    }

    fun interface OnItemFocusChangedListener {
        fun onItemFocusChanged(
            parent: LeanbackRecyclerView,
            itemView: View,
            position: Int,
            hasFocus: Boolean
        )
    }

    private var itemClickListener: OnItemClickListener? = null
    private var itemFocusChangeListener: OnItemFocusChangedListener? = null

    //焦点记忆帮助类
    private val memoryHelper = RecyclerViewMemoryHelper(this, attrs, defStyleAttr)

    //是否在滑动的时候分发KeyEvent，默认不分发，避免NPE导致崩溃
    private var dispatchKeyEventOnScrolling: Boolean = false

    //滑动之后待请求焦点的位置
    private var pendingFocusPosition = NO_POSITION

    private val itemClickGlue = OnClickListener {
        val position = getChildAdapterPosition(it)
        itemClickListener?.onItemClick(this, it, position)
    }

    private val itemFocusChangeGlue = OnFocusChangeListener { v, hasFocus ->
        val position = getChildAdapterPosition(v)
        itemFocusChangeListener?.onItemFocusChanged(this, v, position, hasFocus)
    }

    private val scrollListener: OnScrollListener = object : OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == SCROLL_STATE_IDLE) {
                onScrollStateIDLE()
            }
        }
    }

    init {
        val ta = context.obtainStyledAttributes(
            attrs,
            R.styleable.LeanbackRecyclerView,
            defStyleAttr,
            0
        )
        dispatchKeyEventOnScrolling = ta.getBoolean(
            R.styleable.LeanbackRecyclerView_rv_dispatchKeyEventOnScrolling_bas,
            dispatchKeyEventOnScrolling
        )

        ta.recycle()
        this.addOnScrollListener(scrollListener)
    }

    /**
     * 设置是否在滚动的时候分发KeyEvent，默认不分发，消耗掉事件（避免出现按键不放导致滚动出现空异常）
     */
    fun setDispatchKeyEventOnScrolling(isEnable: Boolean) {
        this.dispatchKeyEventOnScrolling = isEnable
    }

    /**
     * 设置ItemView点击事件
     */
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    /**
     * 设置ItemView焦点变化事件
     */
    fun setOnItemFocusChangeListener(listener: OnItemFocusChangedListener) {
        this.itemFocusChangeListener = listener
    }

    override fun onChildAttachedToWindow(child: View) {
        super.onChildAttachedToWindow(child)
        //绑定点击事件
        if (itemClickListener != null && child.isClickable && !ViewCompat.hasOnClickListeners(child)) {
            child.setOnClickListener(itemClickGlue)
        }
        //绑定焦点改变事件
        if (itemFocusChangeListener != null && child.isFocusable && child.onFocusChangeListener == null) {
            child.onFocusChangeListener = itemFocusChangeGlue
        }
    }

    override fun requestChildFocus(child: View?, focused: View?) {
        super.requestChildFocus(child, focused)
        memoryHelper.requestChildFocus(child, focused)
    }

    override fun addFocusables(views: ArrayList<View>?, direction: Int, focusableMode: Int) {
        if (memoryHelper.addFocusables(views, direction, focusableMode))
            return
        super.addFocusables(views, direction, focusableMode)
    }

    override fun onRequestFocusInDescendants(
        direction: Int,
        previouslyFocusedRect: Rect?
    ): Boolean {
        if (memoryHelper.onRequestFocusInDescendants(direction, previouslyFocusedRect)) {
            return true
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (!dispatchKeyEventOnScrolling && isScrolling())
            return true
        return super.dispatchKeyEvent(event)
    }

    /**
     * 平滑滚动到指定位置，并请求焦点
     * @param scrollToPosition 滑动位置
     * @param focusPosition 请求焦点的位置
     */
    fun smoothScrollToPositionAndRequestFocus(scrollToPosition: Int, focusPosition: Int) {
        pendingFocusPosition = focusPosition
        findViewHolderForAdapterPosition(scrollToPosition)?.let {
            //滚动位置可见，列表不会真正滚动，此时处理焦点请求
            handlePendingFocusPosition()
        } != null
        smoothScrollToPosition(scrollToPosition)
    }

    /**
     * 滚动到指定位置并请求焦点
     * @param scrollToPosition 滑动位置
     * @param focusPosition 请求焦点的位置
     */
    fun scrollToPositionAndRequestFocus(
        scrollToPosition: Int,
        focusPosition: Int
    ) {
        pendingFocusPosition = focusPosition
        findViewHolderForAdapterPosition(scrollToPosition)?.let {
            //滚动位置可见，列表不会真正滚动，此时处理焦点请求
            handlePendingFocusPosition()
        } != null
        scrollToPosition(scrollToPosition)
    }

    //停止滚动
    private fun onScrollStateIDLE() {
        handlePendingFocusPosition()
    }

    override fun onViewAdded(child: View) {
        super.onViewAdded(child)
        handlePendingFocusPositionOnViewAdded(child)
    }

    private fun handlePendingFocusPosition() {
        if (pendingFocusPosition == NO_POSITION)
            return

        val holder = findViewHolderForAdapterPosition(pendingFocusPosition) ?: return

        if (this.hasFocus()) {
            holder.itemView.requestFocus()
        }
        pendingFocusPosition = NO_POSITION
    }

    private fun handlePendingFocusPositionOnViewAdded(newView: View) {
        if (pendingFocusPosition == NO_POSITION)
            return

        val adapterPosition = getChildAdapterPosition(newView)

        if (adapterPosition == pendingFocusPosition) {
            newView.requestFocus()
            pendingFocusPosition = NO_POSITION
        }
    }

    private fun isScrolling(): Boolean {
        return scrollState != SCROLL_STATE_IDLE
    }
}