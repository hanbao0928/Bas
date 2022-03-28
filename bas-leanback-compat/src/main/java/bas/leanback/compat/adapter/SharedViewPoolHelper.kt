package bas.leanback.compat.adapter

import androidx.leanback.widget.ItemBridgeAdapter
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.RowPresenter
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Field
import java.util.*

/**
 * Created by Lucio on 2021/5/20.
 * 用于ListRow共享RecycledViewPool（只能用于ListRow情况：即VerticalGridView中ListRow之间共享Item，提高效率）
 */
internal class SharedViewPoolHelper : ItemBridgeAdapter.AdapterListener() {

    /**
     * 用于[ListRowPresenter.ViewHolder.getGridView]共享view pool
     */
    private var sharedRecycledViewPool: RecyclerView.RecycledViewPool? = null

    /**
     * 用于[ListRowPresenter.ViewHolder.getBridgeAdapter]共享presenters
     */
    private var sharedPresenterMapper: ArrayList<Presenter>? = null

    private var chainAdapterListener: ItemBridgeAdapter.AdapterListener? = null


    /**
     * 设置共享的Presenters，用于设置每一个[ListRowPresenter.ViewHolder.getBridgeAdapter]持有的Presenters
     */
    fun setPresenterMapper(presenterMapper: ArrayList<Presenter>) {
        this.sharedPresenterMapper = presenterMapper
    }

    /**
     * 设置Adapter监听
     */
    fun setAdapterListener(listener: ItemBridgeAdapter.AdapterListener?) {
        this.chainAdapterListener = listener
    }

    override fun onAddPresenter(presenter: Presenter?, type: Int) {
        super.onAddPresenter(presenter, type)
        chainAdapterListener?.onAddPresenter(presenter, type)
    }

    override fun onBind(viewHolder: ItemBridgeAdapter.ViewHolder?) {
        super.onBind(viewHolder)
        chainAdapterListener?.onBind(viewHolder)
    }

    override fun onBind(viewHolder: ItemBridgeAdapter.ViewHolder?, payloads: MutableList<Any?>?) {
        super.onBind(viewHolder, payloads)
        chainAdapterListener?.onBind(viewHolder, payloads)
    }

    override fun onUnbind(viewHolder: ItemBridgeAdapter.ViewHolder?) {
        super.onUnbind(viewHolder)
        chainAdapterListener?.onUnbind(viewHolder)
    }

    override fun onAttachedToWindow(viewHolder: ItemBridgeAdapter.ViewHolder?) {
        super.onAttachedToWindow(viewHolder)
        chainAdapterListener?.onAttachedToWindow(viewHolder)
    }

    override fun onDetachedFromWindow(viewHolder: ItemBridgeAdapter.ViewHolder?) {
        super.onDetachedFromWindow(viewHolder)
        chainAdapterListener?.onDetachedFromWindow(viewHolder)
    }

    override fun onCreate(viewHolder: ItemBridgeAdapter.ViewHolder) {
        super.onCreate(viewHolder)
        setupSharedViewPool(viewHolder)
        chainAdapterListener?.onCreate(viewHolder)
    }

    private fun setupSharedViewPool(viewHolder: ItemBridgeAdapter.ViewHolder) {
        //不是Row，不处理
        val rowPresenter = viewHolder.presenter as? RowPresenter ?: return

        val rowViewHolder = rowPresenter.getRowViewHolder(viewHolder.viewHolder)
        if (rowViewHolder is ListRowPresenter.ViewHolder) {
            //让所有的ListRow都用同一个ViewPool，用同一个PresenterMapper，这是共享的关键
            val view = rowViewHolder.gridView
            // Recycled view pool is shared between all list rows
            if (sharedRecycledViewPool == null) {
                sharedRecycledViewPool = view.recycledViewPool
            } else {
                view.setRecycledViewPool(sharedRecycledViewPool)
            }

            // presenters is shared between all list rows
            val bridgeAdapter = rowViewHolder.bridgeAdapter
            if (sharedPresenterMapper == null) {
                sharedPresenterMapper = bridgeAdapter.presenterMapper
            } else {
                bridgeAdapter.presenterMapper = sharedPresenterMapper
            }
        }
    }


    companion object {

        /**
         * 注意，该方法会替换[ItemBridgeAdapter.setAdapterListener],所以适合没有设置AdapterListener的情况，
         * 建议使用[SharedViewPoolItemBridgeAdapter]
         */
        @Deprecated("建议使用[SharedViewPoolItemBridgeAdapter]")
        @JvmStatic
        fun apply(adapter: ItemBridgeAdapter) {
            try {
                val clazz: Class<ItemBridgeAdapter> = adapter.javaClass
                val fPop: Field = clazz.getField("mAdapterListener")
                fPop.isAccessible = true
                val userAdapterListener = fPop.get(clazz) as ItemBridgeAdapter.AdapterListener?
                apply(adapter, userAdapterListener)
            } catch (e: Exception) {
                adapter.setAdapterListener(SharedViewPoolHelper())
            }
        }

        /**
         * @param adapter
         * @param userAdapterListener 用户的[ItemBridgeAdapter.AdapterListener]
         */
        @JvmStatic
        fun apply(
            adapter: ItemBridgeAdapter,
            userAdapterListener: ItemBridgeAdapter.AdapterListener? = null
        ) {
            adapter.setAdapterListener(SharedViewPoolHelper().also {
                it.setAdapterListener(userAdapterListener)
            })
        }
    }

}