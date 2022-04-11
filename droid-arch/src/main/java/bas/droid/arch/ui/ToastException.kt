package bas.droid.arch.ui

import androidx.annotation.StringRes
import bas.droid.core.ctxBas

class ToastException(message: String) : RuntimeException(message) {
    constructor(@StringRes textId: Int) : this(ctxBas.getString(textId))
}