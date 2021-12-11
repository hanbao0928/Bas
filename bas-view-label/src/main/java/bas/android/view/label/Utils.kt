package bas.android.view.label

import kotlin.properties.Delegates
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Lucio on 2021/12/11.
 */
inline fun <T> Delegates.smartObservable(
    initialValue: T,
    crossinline onChanged: (property: KProperty<*>, oldValue: T, newValue: T) -> Unit
): ReadWriteProperty<Any?, T> =
    object : ObservableProperty<T>(initialValue) {

        override fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Boolean =
            oldValue != newValue


        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) =
            onChanged(property, oldValue, newValue)
    }
