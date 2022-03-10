package bas.droid.core.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import bas.droid.core.util.checkValidationOrThrow
import bas.lib.core.lang.onCatch
import bas.lib.core.lang.tryIgnore

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

fun Context.startActivitySafely(intent: Intent) {
    tryIgnore {
        intent.checkValidationOrThrow(this)
        if (this !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        this.startActivity(intent)
    }.onCatch {
        Log.w(this::class.java.simpleName, "无法打开指定Intent", it)
    }
}

fun Activity.startActivityForResultSafely(intent: Intent, requestCode: Int) {
    tryIgnore {
        intent.checkValidationOrThrow(this)
        this.startActivityForResult(intent, requestCode)
    }.onCatch {
        Log.w(this::class.java.simpleName, "无法打开指定Intent", it)
    }
}