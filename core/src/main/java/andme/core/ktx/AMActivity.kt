/**
 * Created by Lucio on 2020-11-01.
 */

package andme.core.ktx

import andme.core.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import androidx.annotation.AttrRes


/**
 * 快速运行一个Activity
 */
fun Context.startActivity(clazz: Class<out Activity>) {
    val it = Intent(this, clazz)
    if(this !is Activity)
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(it)
}


/**
 * 获取主题中的资源
 * @return
 */
fun Context.getThemeAttrValue(@AttrRes attr:Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}

/**
 * 获取背景色
 */
fun Context.getBackgroundColor():Int{
    return getThemeAttrValue(R.attr.backgroundColor)
}

/**
 * 获取主题色
 */
fun Context.getPrimaryColor():Int{
    return getThemeAttrValue(R.attr.colorPrimary)
}

