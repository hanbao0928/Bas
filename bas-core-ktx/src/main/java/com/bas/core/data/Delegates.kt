package com.bas.core.data

import kotlin.properties.Delegates
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Lucio on 2021/11/26.
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

/**
 * 缓存属性：即获取数据时，如果数据超过了[expires]设置的时间，则返回null，
 * @param expires 过期时间，单位毫秒。
 * 设置数据后重置计时。
 * @param initialValue 初始值
 */
inline fun <T> Delegates.cache(expires: Long, initialValue: T? = null): ReadWriteProperty<Any, T?> {
    return CacheProperty(expires, initialValue)
}


/**
 * 可以更新的缓存属性：即获取数据时，如果数据超过了[expires]设置的时间，则重新调用acquirer获取最新数据
 * @param expires 过期时间，单位毫秒
 * @param initialValue 初始值
 * @param acquirer 数据提供器
 */
inline fun <T> Delegates.cacheUpdatable(
    expires: Long,
    initialValue: T? = null,
    noinline acquirer: () -> T?
): ReadOnlyProperty<Any, T?> {
    return CacheUpdatableProperty(expires, initialValue, acquirer)
}
