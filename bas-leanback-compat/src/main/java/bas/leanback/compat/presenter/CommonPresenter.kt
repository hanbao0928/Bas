package bas.leanback.compat.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Lucio on 2021/12/9.
 */
abstract class CommonPresenter<D> private constructor(
    val layoutId: Int,
    val itemViewCreator: ((ViewGroup) -> View)?
) :
    BasePresenter<D, CommonPresenter.CommonViewHolder>() {

    constructor(layoutId: Int) : this(layoutId, null)

    constructor(itemViewCreator: (ViewGroup) -> View) : this(0, itemViewCreator)

    /**
     * Creates a new [View].
     */
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val holder = if (layoutId > 0) {
            CommonViewHolder(layoutId, parent)
        } else {
            CommonViewHolder(itemViewCreator!!.invoke(parent))
        }
        onViewHolderCreated(holder)
        return holder
    }

    protected open fun onViewHolderCreated(holder: CommonViewHolder) {

    }

    open class CommonViewHolder : BaseViewHolder {
        constructor(view: View) : super(view)
        constructor(layoutId: Int, parent: ViewGroup) : super(layoutId, parent)
        constructor(inflater: LayoutInflater, layoutId: Int, parent: ViewGroup) : super(
            inflater,
            layoutId,
            parent
        )
    }

}

