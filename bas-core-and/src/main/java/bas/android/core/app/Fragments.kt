package bas.android.core.app

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment
import bas.android.core.util.checkValidationOrThrow
import bas.lang.onCatch
import bas.lang.tryIgnore

/**
 * Created by Lucio on 2021/12/1.
 */

/**
 * 快速运行一个Activity
 */
fun Fragment.startActivity(clazz: Class<out Activity>) {
    val it = Intent(this.requireContext(), clazz)
    startActivity(it)
}

fun Fragment.startActivitySafely(intent: Intent) {
    tryIgnore {
        intent.checkValidationOrThrow(this.requireContext())
        this.startActivity(intent)
    }.onCatch {
        Log.w(this::class.java.simpleName, "无法打开指定Intent", it)
    }
}

fun Fragment.startActivityForResultSafely(intent: Intent, requestCode: Int) {
    tryIgnore {
        intent.checkValidationOrThrow(this.requireContext())
        this.startActivityForResult(intent, requestCode)
    }.onCatch {
        Log.w(this::class.java.simpleName, "无法打开指定Intent", it)
    }
}