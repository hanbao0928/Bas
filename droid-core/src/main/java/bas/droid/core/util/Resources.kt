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
        return 25.dpInt
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

inline fun Context.getIdentifier(type: String, name: String): Int {
    return resources.getIdentifier(name, type, packageName)
}

/**
 * 获取 drawable资源ID
 * @param resName drawable 的名称
 */
inline fun Context.getDrawableId(resName: String): Int {
    return getIdentifier("drawable", resName)
}

fun Context.getDrawable(name: String): Drawable? {
    val resId = getDrawableId(name)
    return ResourcesCompat.getDrawable(this.resources, resId, this.theme)
}

/**
 * 获取 layout 布局文件ID
 * @param resName 布局文件名
 */
inline fun Context.getLayoutId(resName: String): Int {
    return getIdentifier("layout", resName)
}

/**
 * 获取 string 对应的ID
 * @param resName string name的名称
 */
inline fun Context.getStringId(resName: String): Int {
    return getIdentifier("string", resName)
}

/**
 * 获取 mipmap资源ID
 * @param resName
 */
inline fun Context.getMipmapId(resName: String): Int {
    return getIdentifier("mipmap", resName)
}


/**
 * 获取 style 资源ID
 * @param resName style的名称
 */
inline fun Context.getStyleId(resName: String): Int {
    return getIdentifier("style", resName)
}

/**
 * 获取 styleable资源ID
 * @param resName styleable 的名称
 */
inline fun Context.getStyleableId(resName: String): Int {
    return getIdentifier("styleable", resName)
}

/**
 * 获取 anim 资源ID
 * @param resName anim xml 文件名称
 */
inline fun Context.getAnimId(resName: String): Int {
    return getIdentifier("anim", resName)
}

/**
 * 获取 id 资源ID
 * @param resName id 的名称
 */
inline fun Context.getId(resName: String): Int {
    return getIdentifier("id", resName)
}

/**
 * color
 * @param resName color 名称
 */
inline fun Context.getColorId(resName: String): Int {
    return getIdentifier("color", resName)
}

