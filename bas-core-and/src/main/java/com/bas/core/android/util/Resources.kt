package com.bas.core.android.util

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.bas.core.android.basCtx

/**
 * Created by Lucio on 2021/10/12.
 */


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


inline val Int.dp get() = this * basCtx.resources.displayMetrics.density
inline val Int.sp get() = this * basCtx.resources.displayMetrics.scaledDensity

inline val Float.dp get() = this * basCtx.resources.displayMetrics.density
inline val Float.sp get() = this * basCtx.resources.displayMetrics.scaledDensity

