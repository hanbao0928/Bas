@file:JvmName("ResourcesKt")
@file:JvmMultifileClass

/**
 * Created by Lucio on 2022/3/4.
 */

package bas.droid.core.util

import android.content.Context
import android.view.View
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import bas.droid.core.ctxBas


inline val density get() = ctxBas.resources.displayMetrics.density
inline val scaledDensity get() = ctxBas.resources.displayMetrics.scaledDensity

inline val Int.dp: Int get() = toFloat().dp

inline val Float.dp: Int get() = (this * density).toInt() //todo 是否需要+ 0.5f再取整？

inline val Double.dp: Int get() = toFloat().dp

inline val Int.sp: Int get() = toFloat().sp

inline val Float.sp: Int get() = (this * scaledDensity).toInt() //todo 是否需要+ 0.5f再取整？

inline val Double.sp: Int get() = this.toFloat().sp

inline fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()
inline fun Context.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()
inline fun Context.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()
inline fun Context.sp(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()
inline fun Context.px2dip(px: Int): Float = px.toFloat() / resources.displayMetrics.density
inline fun Context.px2sp(px: Int): Float = px.toFloat() / resources.displayMetrics.scaledDensity
inline fun Context.dimen(@DimenRes resource: Int): Int = resources.getDimensionPixelSize(resource)


inline fun View.dip(value: Int): Int = context.dip(value)
inline fun View.dip(value: Float): Int = context.dip(value)
inline fun View.sp(value: Int): Int = context.sp(value)
inline fun View.sp(value: Float): Int = context.sp(value)
inline fun View.px2dip(px: Int): Float = context.px2dip(px)
inline fun View.px2sp(px: Int): Float = context.px2sp(px)
inline fun View.dimen(@DimenRes resource: Int): Int = context.dimen(resource)

inline fun Fragment.dip(value: Int): Int = requireContext().dip(value)
inline fun Fragment.dip(value: Float): Int = requireContext().dip(value)
inline fun Fragment.sp(value: Int): Int = requireContext().sp(value)
inline fun Fragment.sp(value: Float): Int = requireContext().sp(value)
inline fun Fragment.px2dip(px: Int): Float = requireContext().px2dip(px)
inline fun Fragment.px2sp(px: Int): Float = requireContext().px2sp(px)
inline fun Fragment.dimen(@DimenRes resource: Int): Int = requireContext().dimen(resource)

