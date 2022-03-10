/**
 * Created by Lucio on 2021/10/12.
 */
@file:JvmName("ResourcesKt")
@file:JvmMultifileClass
package bas.droid.core.util

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.res.ResourcesCompat
import bas.droid.core.ctxBas

/**
 * 屏幕宽度
 */
inline val screenWidth: Int get() = ctxBas.resources.displayMetrics.widthPixels

/**
 * 屏幕高度
 */
inline val screenHeight: Int get() = ctxBas.resources.displayMetrics.heightPixels

/**
 * 获取可用的状态栏高度；通过res读取状态高度，如果没读取到并且当前系统大于19，则默认25dp;
 * 建议使用该方法
 * @see getStatusBarHeight
 */
fun getAvailableStatusBarHeight(ctx: Context): Int {
    val size = getStatusBarHeight(ctx)
    if (size == 0 && Build.VERSION.SDK_INT >= 19) {
        //如果获取系统状态栏高度失败，并且设备系统大于19，则默认25dp
        return 25.dp
    }
    return size
}

/**
 * 获取状态栏高度;通过资源读取状态栏高度
 */
fun getStatusBarHeight(ctx: Context): Int {

    return ctx.applicationContext.resources.run {
        var result = 0
        try {
            val resourceId = this.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = this.getDimensionPixelSize(resourceId)
            }
        } catch (e: Resources.NotFoundException) {
        }
        result
    }
}

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



