package bas.android.core.app

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * Created by Lucio on 2021/12/1.
 */

/**
 * 快速运行一个Activity
 */
fun Context.startActivity(clazz: Class<out Activity>) {
    val it = Intent(this, clazz)
    if(this !is Activity)
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(it)
}

fun Activity.startActivity(clazz: Class<out Activity>) {
    val it = Intent(this, clazz)
    startActivity(it)
}
