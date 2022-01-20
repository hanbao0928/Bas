package bas.android.arch.app

import androidx.fragment.app.Fragment

/**
 * Created by Lucio on 2021/12/6.
 */
open class BasFragment : Fragment {
    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

}