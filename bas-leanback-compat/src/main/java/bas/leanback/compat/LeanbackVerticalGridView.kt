package bas.leanback.compat

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.leanback.widget.OnChildViewHolderSelectedListener
import androidx.leanback.widget.VerticalGridView
import bas.leanback.compat.focussearch.AbstractGridViewFocusSearchHelper
import bas.leanback.compat.memory.GridViewMemoryHelper
import bas.leanback.layout.memory.MemoryState
import java.util.*

/**
 * Created by Lucio on 2021/3/31.
 */
class LeanbackVerticalGridView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : VerticalGridView(context, attrs, defStyleAttr), AbstractGridViewFocusSearchHelper.Callback {

    private val memoryHelper: GridViewMemoryHelper =
        GridViewMemoryHelper.create(this, attrs, defStyleAttr)

    private val focusSearchHelper: AbstractGridViewFocusSearchHelper<VerticalGridView> =
        AbstractGridViewFocusSearchHelper.create(context, this, this, attrs, defStyleAttr)

    @MemoryState
    var memoryViewState: Int
        get() = memoryHelper.memoryState
        set(value) {
            memoryHelper.memoryState = value
        }

    var isMemoryEnabled: Boolean
        get() = memoryHelper.isMemoryEnabled
        set(value) {
            memoryHelper.isMemoryEnabled = value
        }

    override fun requestChildFocus(child: View?, focused: View?) {
        super.requestChildFocus(child, focused)
        memoryHelper.requestChildFocus(child, focused)
    }

    override fun onViewRemoved(child: View?) {
        super.onViewRemoved(child)
        memoryHelper.onViewRemoved(child)
    }

    @Deprecated(
        "请使用addOnChildViewHolderSelectedListener",
        replaceWith = ReplaceWith("addOnChildViewHolderSelectedListener")
    )
    override fun setOnChildViewHolderSelectedListener(listener: OnChildViewHolderSelectedListener?) {
        super.setOnChildViewHolderSelectedListener(listener)
        //设置监听之后会移除之前添加的所有监听，因此要重新添加焦点记忆监听
        addOnChildViewHolderSelectedListener(memoryHelper.childViewHolderSelectedListener)
    }

    @SuppressLint("RestrictedApi")
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

    /**
     * Notify layout manager that layout directionality has been updated
     */
    override fun onRtlPropertiesChanged(layoutDirection: Int) {
        super.onRtlPropertiesChanged(layoutDirection)
        focusSearchHelper.onRtlPropertiesChanged(layoutDirection)
    }

    override fun focusSearch(focused: View?, direction: Int): View? {
        return focusSearchHelper.focusSearch(focused, direction)
    }

    override fun callSuperFocusSearch(focused: View?, direction: Int): View? {
        return super.focusSearch(focused, direction)
    }

    //    protected override fun onRequestFocusInDescendants(
//        direction: Int,
//        previouslyFocusedRect: Rect?
//    ): Boolean {
//        //焦点记忆
//        val position: Int =
//            if (mSelectedPosition == NO_POSITION || !mIsMemoryFocus) getFirstVisibleAndFocusablePosition() else mSelectedPosition
//        val child =
//            if (null != layoutManager) layoutManager!!.findViewByPosition(position) else null
//        if (null != child) {
//            if (null != onFocusChangeListener) {
//                onFocusChangeListener.onFocusChange(this, true)
//            }
//            return child.requestFocus(direction, previouslyFocusedRect)
//        }
//        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect)
//    }


    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
    }


    override fun swapAdapter(adapter: Adapter<*>?, removeAndRecycleExistingViews: Boolean) {
        super.swapAdapter(adapter, removeAndRecycleExistingViews)
    }




}