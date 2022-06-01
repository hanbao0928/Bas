package bas.droid.arch

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import bas.droid.core.util.isNetworkConnected

/**
 * Created by Lucio on 2022/3/23.
 */
open class AndroidViewModelArch(application: Application) : AndroidViewModel(application) {

    @SuppressLint("MissingPermission")
    protected fun isInternetConnection():Boolean  = getApplication<Application>().isNetworkConnected()
}