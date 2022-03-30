package bas.leanback.recyclerview

import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import bas.leanback.core.MemoryState
import bas.lib.core.data.smartObservable
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by Lucio on 2022/2/8.
 */
class RecyclerViewMemoryHelper(
    val layout: RecyclerView,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.recyclerViewStyle,
) {

    /**
     * 是否启用焦点记忆
     */
    var isMemoryEnabled: Boolean by Delegates.smartObservable(DEFAULT_MEMORY_ENABLED) { _, oldValue, newValue ->
        if (!newValue) {
            onMemoryEnableChanged()
        }
    }

    /**
     * 焦点记忆View使用的状态类型状态
     */
    @get:MemoryState
    var memoryState: Int by Delegates.smartObservable(DEFAULT_MEMORY_STATE) { _, oldValue, newValue ->
        //属性发生了变化
        onMemoryStateChanged()
    }

    private var rememberedChildView: View? = null
    private var rememberedFocusedView: View? = null
    private var rememberedAdapterPosition: Int = NO_POSITION

    init {
        log("开始解析xml属性")
        val ta = layout.context.obtainStyledAttributes(
            attrs,
            R.styleable.LeanbackRecyclerView,
            defStyleAttr,
            0
        )
        isMemoryEnabled = ta.getBoolean(
            R.styleable.LeanbackRecyclerView_rv_enableFocusMemory_bas,
            DEFAULT_MEMORY_ENABLED
        )
        memoryState =
            ta.getInt(
                R.styleable.LeanbackRecyclerView_rv_focusMemoryStateType_bas,
                DEFAULT_MEMORY_STATE
            )
        log("解析结果：isMemoryEnabled=${isMemoryEnabled} memoryState=${memoryState}")
        ta.recycle()
    }

    protected open fun onMemoryEnableChanged() {
        //notify changed :nothing
    }

    protected open fun onMemoryStateChanged() {
        //notify changed :nothing
    }

    /**
     * 是否处理焦点分发：如果当前未启用焦点记忆或者当前控件已经具备焦点,则不采用焦点记忆规则
     * @return true:应该处理焦点分发规则
     */
    @SuppressLint("RestrictedApi")
    protected open fun shouldHandleFocusMemory(): Boolean {
        return isMemoryEnabled && layout.layoutManager != null && rememberedAdapterPosition != NO_POSITION
    }

    /**
     * child请求焦点
     * 在自定义[ViewGroup.requestChildFocus]对应方法中回调该方法
     * @param child 子view
     * @param focused 获取焦点的子/孙view
     */
    @bas.lib.core.lang.annotation.CallByOwner
    fun requestChildFocus(child: View?, focused: View?) {
        if (child == null || focused == null)
            return
        handleChildRequestFocus(child, focused)
    }

    private fun handleChildRequestFocus(child: View, focused: View) {
        if (!isMemoryEnabled) {
            resetRemembered()
            return
        }
        val adapterPosition = layout.getChildAdapterPosition(child)
        if (adapterPosition >= 0) {
            if (child != rememberedChildView) {
                rememberedChildView?.let {
                    setViewMemoryState(it, false)
                }
                setViewMemoryState(child, true)
            }
            rememberedChildView = child
            rememberedFocusedView = focused
            rememberedAdapterPosition = adapterPosition
        } else {
            rememberedChildView?.let {
                setViewMemoryState(it, false)
            }
            resetRemembered()
        }
    }

    private fun resetRemembered() {
        rememberedChildView = null
        rememberedFocusedView = null
        rememberedAdapterPosition = NO_POSITION
    }

    private fun setViewMemoryState(view: View, hasState: Boolean) {
        when (memoryState) {
            MemoryState.SELECTED -> {
                view.isSelected = hasState
            }
            MemoryState.ACTIVATED -> {
                view.isActivated = hasState
            }
            else -> {
            }
        }
    }

    /**
     * 添加可以获取焦点的View
     * 在自定义[ViewGroup.addFocusables]对应方法中回调该方法
     * @return 如果处理了焦点记忆，则返回true，否则返回false
     */
    @bas.lib.core.lang.annotation.CallByOwner
    fun addFocusables(
        views: ArrayList<View>?,
        direction: Int,
        focusableMode: Int
    ): Boolean {
        if (layout.hasFocus() || !shouldHandleFocusMemory()) {
            log("addFocusables：不使用焦点记忆功能（当前Layout已获取焦点：内部焦点查找）、或当前未启用焦点记忆功能")
            return false
        }

        val viewHolder =
            layout.findViewHolderForAdapterPosition(rememberedAdapterPosition) ?: return false
        val pendingChildView = viewHolder.itemView

        val preFocusedView = rememberedFocusedView
        val preChildView = rememberedChildView

        if (pendingChildView == preChildView && preFocusedView != null) {
            //记住的ChildView可用
            log("addFocusables：焦点记忆查找成功 $preFocusedView")
            //添加焦点记忆寻找的ChildView
            views?.add(preFocusedView)
            return true
        }

        if (pendingChildView.canTakeFocus) {
            log("addFocusables：焦点记忆查找成功")
            //添加焦点记忆寻找的ChildView
            views?.add(pendingChildView)
            return true
        }
        log("addFocusables：焦点记忆查找失败")
        return false
    }

    /**
     * @return 处理了焦点分发则返回true，否则返回false
     */
    @bas.lib.core.lang.annotation.CallByOwner
    fun onRequestFocusInDescendants(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        if (!shouldHandleFocusMemory()) {
            log("onRequestFocusInDescendants：不启用焦点记忆分发")
            return false
        }

        val viewHolder =
            layout.findViewHolderForAdapterPosition(rememberedAdapterPosition) ?: return false
        val pendingChildView = viewHolder.itemView

        val preFocusedView = rememberedFocusedView
        val preChildView = rememberedChildView

        if (pendingChildView == preChildView && preFocusedView != null) {
            //记住的ChildView可用
            log("onRequestFocusInDescendants：焦点记忆查找成功 $preFocusedView")
            preFocusedView.requestFocus(direction)
            return true
        }

        if (pendingChildView.canTakeFocus && !pendingChildView.hasFocus()) {
            log("onRequestFocusInDescendants：焦点记忆查找成功 $pendingChildView")
            pendingChildView.requestFocus(direction)
            return true
        }

        return false
    }

    private fun log(msg: String) {
        Log.d(TAG, msg)
    }

    companion object {

        private const val TAG = "RecyclerViewMemory"

        private const val NO_POSITION = RecyclerView.NO_POSITION

        private const val DEFAULT_MEMORY_ENABLED = false

        private const val DEFAULT_MEMORY_STATE = MemoryState.SELECTED

        /**
         * 能否获取焦点
         */
        private inline val View.canTakeFocus: Boolean
            get() = isFocusable && isVisible && isEnabled
    }
}