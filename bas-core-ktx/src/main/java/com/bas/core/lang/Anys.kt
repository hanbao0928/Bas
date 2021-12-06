package com.bas.core.lang

import kotlin.properties.Delegates
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Lucio on 2021/9/25.
 */

inline fun <T> T?.orDefault(initializer: () -> T): T = this ?: initializer()

inline fun <T> T?.orDefault(default:T): T = this ?: default

inline fun <T, R> T.mapAs(transformer: T.() -> R): R {
    return transformer(this)
}

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



