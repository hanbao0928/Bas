package com.bas.core.android.util

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat

/**
 * Created by Lucio on 2021/10/12.
 */

fun Context.getDrawable(name: String): Drawable? {
    return resources.getDrawable(this, name)
}

fun Resources.getDrawable(ctx: Context, name: String): Drawable? {
    val resId = getIdentifier(name, "drawable", ctx.packageName)
    return ResourcesCompat.getDrawable(this, resId, ctx.theme)
}