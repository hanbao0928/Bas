package bas.leanback.compat.memory

import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.leanback.widget.BaseGridView
import androidx.leanback.widget.OnChildViewHolderSelectedListener
import androidx.recyclerview.widget.RecyclerView
import bas.leanback.compat.logd
import bas.leanback.layout.memory.MemoryHelper
import bas.android.core.view.extensions.canTakeFocus
import java.util.*

/**
 * Created by Lucio on 2021/4/18.
 *
 * GridView 或者 RV跟常规的MemoryHelper焦点记忆处理不相同的地方就是如果adapter刷新，会导致child view 移除再添加，
 * 此时常规处理方式焦点记忆便失效了，因此应该通过position去处理焦点记忆
 */
internal class GridViewMemoryHelper private constructor(
    override val viewGroup: BaseGridView,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MemoryHelper(viewGroup, attrs, defStyleAttr) {

    /**
     * 焦点记忆的位置
     */
    var focusMemoryPosition: Int = NO_POSITION

    internal val childViewHolderSelectedListener = object : OnChildViewHolderSelectedListener() {
        /**
         * 焦点记忆位置观察
         */
        override fun onChildViewHolderSelected(
            parent: RecyclerView?,
            child: RecyclerView.ViewHolder?,
            position: Int,
            subposition: Int
        ) {
            super.onChildViewHolderSelected(parent, child, position, subposition)
            if (isMemoryEnabled) {
                focusMemoryPosition = position
                child?.itemView?.let {
                    applyMemoryState(it, memoryState)
                }
            } else {
                focusMemoryPosition = NO_POSITION
            }
            logd("$viewGroup 当前记忆位置：$focusMemoryPosition")
        }
    }

    init {
        viewGroup.addOnChildViewHolderSelectedListener(childViewHolderSelectedListener)
    }
    
    /**
     * 是否处理焦点分发：如果当前未启用焦点记忆或者当前控件已经具备焦点或者当前focus策略是[BaseGridView.FOCUS_SCROLL_ALIGNED],则不采用焦点记忆规则
     * [BaseGridView.FOCUS_SCROLL_ALIGNED]默认会内部处理焦点记忆
     * @return true:应该处理焦点分发规则
     */
    @SuppressLint("RestrictedApi")
    protected fun shouldHandleGridViewFocusMemory(): Boolean {
        return viewGroup.focusScrollStrategy != BaseGridView.FOCUS_SCROLL_ALIGNED 
                && focusMemoryPosition != NO_POSITION
                && viewGroup.layoutManager != null
    }


    /**
     * 添加可以获取焦点的View
     * 在自定义[ViewGroup.addFocusables]对应方法中回调该方法
     * @return 如果处理了焦点记忆，则返回true，否则返回false
     */
    override fun addFocusables(
        views: ArrayList<View>?,
        direction: Int,
        focusableMode: Int
    ): Boolean {
        if (!shouldHandleGridViewFocusMemory()) {
            logd("GridViewMemoryHelper addFocusables：不启用焦点记忆")
            return false
        }

        val handled = super.addFocusables(views, direction, focusableMode)
        if (handled) {
            //如果通过memory view处理了，则说明适配器没有变化
            return handled
        }

        val viewHolder =
            viewGroup.findViewHolderForAdapterPosition(focusMemoryPosition) ?: return false

        val pendingFocusView = viewHolder.itemView
        if (pendingFocusView.canTakeFocus) {
            logd("GridViewMemoryHelper addFocusables：焦点记忆查找成功")
            //添加焦点记忆寻找的ChildView
            views?.add(pendingFocusView)
            return true
        }
        logd("GridViewMemoryHelper addFocusables：焦点记忆查找失败")
        return false
    }

    override fun onRequestFocusInDescendants(
        direction: Int,
        previouslyFocusedRect: Rect?
    ): Boolean {
        if (!shouldHandleGridViewFocusMemory()) {
            logd("GridViewMemoryHelper onRequestFocusInDescendants：不启用焦点记忆分发")
            return false
        }

        val handled = super.onRequestFocusInDescendants(direction, previouslyFocusedRect)
        if (handled) {
            //如果通过memory view处理了，则说明适配器没有变化
            return handled
        }

        val viewHolder =
            viewGroup.findViewHolderForAdapterPosition(focusMemoryPosition) ?: return false

        val pendingFocusView = viewHolder.itemView

        if (pendingFocusView.canTakeFocus) {
            logd("GridViewMemoryHelper onRequestFocusInDescendants：焦点记忆查找成功")
            //添加焦点记忆寻找的ChildView
            pendingFocusView.requestFocus(direction)
            return true
        }
        return false
    }

    companion object {
        
        const val NO_POSITION = BaseGridView.NO_POSITION
        
        @JvmStatic
        fun create(
            view: BaseGridView,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0
        ): GridViewMemoryHelper {
            return GridViewMemoryHelper(view, attrs, defStyleAttr)
        }
    }


}