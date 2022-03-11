package andme.integration.support.modual

import bas.droid.core.util.layoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Lucio on 2021/3/17.
 */
class XPopupViewProvider {

    private var layoutId: Int = 0

    protected lateinit var contentView: View
        private set

    constructor(layoutId: Int) {
        this.layoutId = layoutId
    }

    constructor(contentView: View) {
        this.contentView = contentView
    }

    open fun onCreateView(parent: ViewGroup): View {
        return if (layoutId > 0) {
            parent.context.layoutInflater.inflate(layoutId, parent, false)
        } else {
            return contentView
        }
    }
}
