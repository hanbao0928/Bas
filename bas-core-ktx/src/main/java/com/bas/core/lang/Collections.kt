/**
 * Created by Lucio on 2020/12/13.
 */
package com.bas.core.lang

inline fun <E> MutableCollection<E>.appendAllIfNotNull(
    elements: Collection<E>?,
    allowItemNull: Boolean = true
) = CollectionUtils.addAllIfNotNull(this, elements, allowItemNull)


inline fun <E> Collection<E>?.areItemsEqual(other: Collection<E>?) =
    CollectionUtils.areItemsEqual(this, other)
