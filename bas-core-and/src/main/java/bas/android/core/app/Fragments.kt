package bas.android.core.app

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment

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
