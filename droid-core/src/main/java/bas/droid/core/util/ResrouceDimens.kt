@file:JvmName("ResourcesKt")
@file:JvmMultifileClass

/**
 * Created by Lucio on 2022/3/4.
 */

package bas.droid.core.util

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.annotation.DimenRes
import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import bas.droid.core.ctxBas

inline val ctxInject: Context get() = ctxBas

inline fun Context.dip(value: Float): Float = unitValue(TypedValue.COMPLEX_UNIT_DIP, value)
inline fun Context.dipInt(value: Float): Int = dip(value).toInt()
inline fun Context.dip(value: Int): Float = dip(value.toFloat())
inline fun Context.dipInt(value: Int): Int = dipInt(value.toFloat())
inline fun Context.sp(value: Float): Float = unitValue(TypedValue.COMPLEX_UNIT_SP, value)
inline fun Context.spInt(value: Float): Int = sp(value).toInt()
inline fun Context.sp(value: Int): Float = sp(value.toFloat())
inline fun Context.spInt(value: Int): Int = spInt(value.toFloat())
inline fun Context.dimen(@DimenRes resource: Int): Int = resources.getDimensionPixelSize(resource)


inline fun Context.px2dip(px: Int): Float = px.toFloat() / resources.displayMetrics.density
inline fun Context.px2sp(px: Int): Float = px.toFloat() / resources.displayMetrics.scaledDensity

//Number扩展的基础方法（依赖库注入的Context）
inline val Float.dp: Float get() = ctxInject.dip(this)
inline val Float.dpInt: Int get() = ctxInject.dipInt(this)
inline val Float.sp: Float get() = ctxInject.sp(this)
inline val Float.spInt: Int get() = ctxInject.spInt(this)

//扩展Number类型（依赖库注入的Context）
inline val Int.dp: Float get() = toFloat().dp
inline val Int.dpInt: Int get() = toFloat().dpInt
inline val Double.dp: Float get() = toFloat().dp
inline val Double.dpInt: Int get() = toFloat().dpInt
inline val Int.sp: Float get() = toFloat().sp
inline val Int.spInt: Int get() = toFloat().spInt
inline val Double.sp: Float get() = toFloat().sp
inline val Double.spInt: Int get() = toFloat().spInt

inline fun View.dip(value: Float): Float = context.dip(value)
inline fun View.dipInt(value: Float): Int = context.dipInt(value)
inline fun View.dip(value: Int): Float = dip(value.toFloat())
inline fun View.dipInt(value: Int): Int =dipInt(value.toFloat())
inline fun View.sp(value: Float): Float = context.sp(value)
inline fun View.spInt(value: Float): Int = context.spInt(value)
inline fun View.sp(value: Int): Float = sp(value.toFloat())
inline fun View.spInt(value: Int): Int = spInt(value.toFloat())
inline fun View.dimen(@DimenRes resource: Int): Int = context.dimen(resource)

inline fun View.px2dip(px: Int): Float = context.px2dip(px)
inline fun View.px2sp(px: Int): Float = context.px2sp(px)

inline fun Fragment.dip(value: Float): Float = requireContext().dip(value)
inline fun Fragment.dipInt(value: Float): Int = requireContext().dipInt(value)
inline fun Fragment.dip(value: Int): Float = dip(value.toFloat())
inline fun Fragment.dipInt(value: Int): Int =dipInt(value.toFloat())
inline fun Fragment.sp(value: Float): Float = requireContext().sp(value)
inline fun Fragment.spInt(value: Float): Int = requireContext().spInt(value)
inline fun Fragment.sp(value: Int): Float = sp(value.toFloat())
inline fun Fragment.spInt(value: Int): Int = spInt(value.toFloat())
inline fun Fragment.dimen(@DimenRes resource: Int): Int = requireContext().dimen(resource)
//
//
//inline fun Fragment.dip(value: Int): Float = requireContext().dip(value)
//inline fun Fragment.dip(value: Float): Float = requireContext().dip(value)
//inline fun Fragment.sp(value: Int): Float = requireContext().sp(value)
//inline fun Fragment.sp(value: Float): Float = requireContext().sp(value)
//inline fun Fragment.px2dip(px: Int): Float = requireContext().px2dip(px)
//inline fun Fragment.px2sp(px: Int): Float = requireContext().px2sp(px)
//inline fun Fragment.dimen(@DimenRes resource: Int): Int = requireContext().dimen(resource)


inline val density get() = ctxInject.resources.displayMetrics.density
inline val scaledDensity get() = ctxInject.resources.displayMetrics.scaledDensity


@IntDef(
    value = [TypedValue.COMPLEX_UNIT_PX, TypedValue.COMPLEX_UNIT_DIP, TypedValue.COMPLEX_UNIT_SP, TypedValue.COMPLEX_UNIT_PT, TypedValue.COMPLEX_UNIT_IN, TypedValue.COMPLEX_UNIT_MM]
)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class ComplexDimensionUnit

inline fun Context.unitValue(@ComplexDimensionUnit unit: Int, value: Float): Float =
    TypedValue.applyDimension(unit, value, this.resources.displayMetrics)

