package com.bas.android.leanback.widget.metro

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.core.view.children
import androidx.gridlayout.widget.GridLayout
import com.bas.android.leanback.R

/**
 * Created by Lucio on 2021/9/30.
 */
class MetroView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : GridLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val INVALID_POSITION = -1
        private const val INVALID_TYPE = -1
        private const val DEFAULT_MAX_SCRAP = 5
        internal const val DEBUG = false
        private  val viewHolderTag = R.id.bas_leanback_id
    }

    private val mViewPool = RecycledViewPool()

    private var mAdapter: Adapter? = null

    private val mAdapterDataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            onAdapterDataChanged()
        }

        /**
         * 数据不可用：重新布局
         */
        override fun onInvalidate() {
            onAdapterDataInvalidate()
        }
    }

    /**
     * 重置Layout
     */
    private fun reset() {
        removeAllViews()
        mViewPool.reset()
    }

    /**
     * 缓存所有childView
     */
    private fun recycledViews() {
        val children = this.children
        removeAllViews()
        children.forEach {
            val holder = it.getTag(viewHolderTag) as? ViewHolder
            if (holder != null)
                mViewPool.putRecycledView(holder)
        }
    }

    private fun prepareLayout(columnCount: Int, rowCount: Int) {
        if (columnCount != this.columnCount || rowCount != this.rowCount) {
            //列数 or 行数不相等，重置布局状态
            reset()
            setColumnCount(columnCount)
            setRowCount(rowCount)
        } else {
            recycledViews()
        }
    }

    fun setAdapter(adapter: Adapter) {
        if (adapter == mAdapter) {
            onAdapterDataChanged()
            return
        }
        mAdapter?.unregisterAdapterDataObserver(mAdapterDataObserver)
        prepareLayout(adapter.columnCount, adapter.rowCount)

        mAdapter = adapter
        adapter.registerAdapterDataObserver(mAdapterDataObserver)
        renderAdapterData()
    }

    private fun onAdapterDataChanged() {

    }

    private fun onAdapterDataInvalidate(){
        recycledViews()
        renderAdapterData()
    }

    private fun renderAdapterData() {
        val adapter = mAdapter ?: return

        val items = adapter.data
        if (items.isNullOrEmpty())
            return

        items.forEachIndexed { index, item ->
            val itemViewType = adapter.getItemViewType(index)
            var viewHolder = mViewPool.getRecycledView(itemViewType)
            if (viewHolder == null) {
                viewHolder = adapter.onCreateViewHolder(this,index, itemViewType)
            }
            val lp = LayoutParams(
                viewHolder.itemView.layoutParams ?: LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
            lp.rowSpec = item.rowSpec
            lp.columnSpec = item.columnSpec
            addView(viewHolder.itemView, lp)

            viewHolder.adapterPosition = index
            adapter.bindViewHolder(viewHolder, index)
        }
    }

    abstract class AdapterDataObserver {

        /**
         * 数据发生了改变：刷新布局
         */
        open fun onChanged() {
            // Do nothing
        }

        /**
         * 数据不可用：重新布局
         */
        open fun onInvalidate(){

        }
    }

    abstract class Adapter(
        val columnCount: Int,
        val rowCount: Int,
        val data: List<MetroCell<*>>
    ) {
        private val mObservers: MutableList<AdapterDataObserver> = mutableListOf()

        abstract fun onCreateViewHolder(parent: ViewGroup,position: Int, viewType: Int): ViewHolder

        fun notifyDataSetChanged() {
            mObservers.forEach {
                it.onChanged()
            }
        }

        fun notifyDataInvalidate(){
            mObservers.forEach {
                it.onInvalidate()
            }
        }

        fun getItemViewType(position: Int): Int {
            return data[position].itemViewType
        }

        fun registerAdapterDataObserver(observer: AdapterDataObserver) {
            mObservers.add(observer)
        }

        fun unregisterAdapterDataObserver(observer: AdapterDataObserver) {
            mObservers.remove(observer)
        }

        abstract fun bindViewHolder(viewHolder: ViewHolder, index: Int)
    }

    class ViewHolder(val itemView: View) {

        init {
            itemView.setTag(viewHolderTag, this)
        }

        var itemViewType: Int = INVALID_TYPE
            internal set

        var adapterPosition: Int = INVALID_POSITION
            internal set

        fun resetInternal() {
            adapterPosition = INVALID_POSITION
//            mFlags = 0
//            mPosition = RecyclerView.NO_POSITION
//            mOldPosition = RecyclerView.NO_POSITION
//            mItemId = RecyclerView.NO_ID
//            mPreLayoutPosition = RecyclerView.NO_POSITION
//            mIsRecyclableCount = 0
//            mShadowedHolder = null
//            mShadowingHolder = null
//            clearPayload()
//            mWasImportantForAccessibilityBeforeHidden = ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO
//            mPendingAccessibilityState = RecyclerView.ViewHolder.PENDING_ACCESSIBILITY_STATE_NOT_SET
//            RecyclerView.clearNestedRecyclerViewIfNotNested(this)
        }
    }

    class RecycledViewPool() {

        class ScrapData {
            val mScrapHeap = mutableListOf<ViewHolder>()
            var mMaxScrap = DEFAULT_MAX_SCRAP
        }

        var mScrap = SparseArray<ScrapData>()

        fun reset() {
            clear()
            mScrap.clear()
        }

        /**
         * Discard all ViewHolders.
         */
        fun clear() {
            for (i in 0 until mScrap.size()) {
                mScrap.valueAt(i).mScrapHeap.clear()
            }
        }

        private fun getScrapDataForType(viewType: Int): ScrapData {
            var scrapData: ScrapData? = mScrap[viewType]
            if (scrapData == null) {
                scrapData = ScrapData()
                mScrap.put(viewType, scrapData)
            }
            return scrapData
        }

        /**
         * Sets the maximum number of ViewHolders to hold in the pool before discarding.
         *
         * @param viewType ViewHolder Type
         * @param max      Maximum number
         */
        fun setMaxRecycledViews(viewType: Int, max: Int) {
            val scrapData: ScrapData = getScrapDataForType(viewType)
            scrapData.mMaxScrap = max
            val scrapHeap = scrapData.mScrapHeap
            while (scrapHeap.size > max) {
                scrapHeap.removeAt(scrapHeap.size - 1)
            }
        }

        /**
         * Add a scrap ViewHolder to the pool.
         *
         *
         * If the pool is already full for that ViewHolder's type, it will be immediately discarded.
         *
         * @param scrap ViewHolder to be added to the pool.
         */
        fun putRecycledView(scrap: ViewHolder) {
            val viewType = scrap.itemViewType
            val scrapHeap = getScrapDataForType(viewType).mScrapHeap
            if (mScrap[viewType].mMaxScrap <= scrapHeap.size) {
                return
            }
            require(!(DEBUG && scrapHeap.contains(scrap))) { "this scrap item already exists" }
            scrap.resetInternal()
            scrapHeap.add(scrap)
        }

        fun getRecycledView(viewType: Int): ViewHolder? {
            val scrapData: ScrapData? = mScrap[viewType]
            if (scrapData != null && scrapData.mScrapHeap.isNotEmpty()) {
                val scrapHeap = scrapData.mScrapHeap
                for (i in scrapHeap.indices.reversed()) {
                    return scrapHeap.removeAt(i)
                }
            }
            return null
        }
    }


}