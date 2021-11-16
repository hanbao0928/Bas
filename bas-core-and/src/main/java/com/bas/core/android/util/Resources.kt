/**
 * Created by Lucio on 2021/10/12.
 */
package com.bas.core.android.util

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.bas.core.android.basCtx


fun Context.getDrawableIdentifier(name: String): Int {
    return resources.getIdentifier(name, "drawable", packageName)
}

fun Context.getDrawable(name: String): Drawable? {
    return resources.getDrawable(this, name)
}

fun Resources.getDrawable(ctx: Context, name: String): Drawable? {
    val resId = getIdentifier(name, "drawable", ctx.packageName)
    return ResourcesCompat.getDrawable(this, resId, ctx.theme)
}

inline val Float.dp: Int
    get() = run {
        return (this * density).toInt() //todo 是否需要+ 0.5f再取整？
    }

inline val Double.dp: Int
    get() = run {
        return toFloat().dp
    }

inline val Int.dp: Int
    get() = run {
        return toFloat().dp
    }

inline val Float.sp: Int
    get() = run {
        return (this * scaledDensity).toInt() //todo 是否需要+ 0.5f再取整？
    }

inline val Double.sp: Int
    get() = run {
        return toFloat().sp
    }

inline val Int.sp: Int
    get() = run {
        return toFloat().sp
    }

inline val density get() = basCtx.resources.displayMetrics.density
inline val scaledDensity get() = basCtx.resources.displayMetrics.scaledDensity