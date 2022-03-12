package bas.leanback.core

import android.util.Log
import kotlin.properties.Delegates
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Lucio on 2021/11/17.
 */

private const val TAG = "LeanbackCore"
private const val DEBUG = true

internal inline fun logd(msg: String) {
    if (!DEBUG)
        return
    Log.d(TAG, msg)
}

internal inline fun logi(msg: String) {
    Log.i(TAG, msg)
}

@JvmOverloads
internal inline fun logw(msg: String, e: Throwable? = null) {
    Log.w(TAG, msg, e)
}

@JvmOverloads
internal inline fun loge(msg: String, e: Throwable? = null) {
    Log.e(TAG, msg, e)
}


/**
 * Created by Lucio on 2021/11/26.
 */
internal inline fun <T> Delegates.smartObservable(
    initialValue: T,
    crossinline onChanged: (property: KProperty<*>, oldValue: T, newValue: T) -> Unit
): ReadWriteProperty<Any?, T> =
    object : ObservableProperty<T>(initialValue) {

        override fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Boolean =
            oldValue != newValue


        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) =
            onChanged(property, oldValue, newValue)
    }