package bas.leanback.compat.adapter

import androidx.leanback.widget.ItemBridgeAdapter
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.RowPresenter
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * Created by Lucio on 2021/5/20.
 * 用于ListRow共享RecycledViewPool（只能用于ListRow情况：即VerticalGridView中ListRow之间共享Item，提高效率）
 */
internal class SharedViewPoolHelper : ItemBridgeAdapter.AdapterListener() {

    private var recycledViewPool: RecyclerView.RecycledViewPool? = null

    private var mPresenterMapper: ArrayList<Presenter>? = null

    var chainAdapterListener: ItemBridgeAdapter.AdapterListener? = null

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
            if (recycledViewPool == null) {
                recycledViewPool = view.recycledViewPool
            } else {
                view.setRecycledViewPool(recycledViewPool)
            }
            val bridgeAdapter = rowViewHolder.bridgeAdapter
            if (mPresenterMapper == null) {
                mPresenterMapper = bridgeAdapter.presenterMapper
            } else {
                bridgeAdapter.presenterMapper = mPresenterMapper
            }
        }
    }


    companion object{

        /**
         * 注意，该方法会替换[ItemBridgeAdapter.setAdapterListener],所以适合没有设置AdapterListener的情况，
         * 建议使用[SharedViewPoolItemBridgeAdapter]
         */
        @Deprecated("建议使用[SharedViewPoolItemBridgeAdapter]")
        @JvmStatic
        fun apply(adapter:ItemBridgeAdapter){
            adapter.setAdapterListener(SharedViewPoolHelper())
        }

    }

}