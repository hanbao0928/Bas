/**
 * Created by Lucio on 2020/12/13.
 */
package com.bas.core.lang

/**
 * 拼接所有非空元素
 */
inline fun <E> MutableCollection<E>.appendAllNotNulls(
    elements: Collection<E>?
): MutableCollection<E> = this.appendAll(elements, true)

/**
 * 拼接元素
 * @param filterNotNulls 是否只过滤非空元素，即是否不添加null元素
 */
inline fun <E> MutableCollection<E>.appendAll(
    elements: Collection<E>?,
    filterNotNulls: Boolean = false
): MutableCollection<E> {
    CollectionUtils.mergeTo(this, elements, filterNotNulls)
    return this
}

inline fun <E> Collection<E>?.areItemsEqual(other: Collection<E>?) =
    CollectionUtils.areItemsEqual(this, other)
