package bas.droid.arch

import android.app.Application
import bas.droid.arch.ui.DroidExceptionMessageTransformer
import bas.lib.core.exceptionMessageTransformer


fun initDroidArch(app: Application) {
    exceptionMessageTransformer = DroidExceptionMessageTransformer()
}