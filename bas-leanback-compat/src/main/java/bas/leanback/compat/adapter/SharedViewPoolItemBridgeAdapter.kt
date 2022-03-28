package bas.leanback.compat.adapter

import androidx.leanback.widget.ItemBridgeAdapter
import androidx.leanback.widget.ObjectAdapter
import androidx.leanback.widget.PresenterSelector

/**
 * Created by Lucio on 2021/5/20.
 * Item共享ViewPool的[ItemBridgeAdapter]
 */
class SharedViewPoolItemBridgeAdapter : ItemBridgeAdapter {

    private val viewPoolShareHelper = SharedViewPoolHelper()

    constructor(adapter: ObjectAdapter?, presenterSelector: PresenterSelector?) : super(
        adapter,
        presenterSelector
    )

    constructor(adapter: ObjectAdapter?) : super(adapter)
    constructor() : super()

    init {
        super.setAdapterListener(viewPoolShareHelper)
    }

    override fun setAdapterListener(listener: AdapterListener?) {
        viewPoolShareHelper.setAdapterListener(listener)
    }
}