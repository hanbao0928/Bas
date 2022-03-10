package bas.droid.core.view.extensions

import android.view.View
import android.widget.TextView

/**
 * Created by Lucio on 2021/11/14.
 */
fun TextView.setTextOrGone(message: CharSequence?) {
    visibility = if (message.isNullOrEmpty()) {
        View.GONE
    } else {
        View.VISIBLE
    }
    text = message
}

/**
 * 绑定文本；文本为空时隐藏控件，在文本不为空时并执行[visibleInvoke]回调
 */
inline fun TextView.setTextOrGone(
    message: CharSequence?,
    visibleInvoke: TextView.() -> Unit
) {
    if (message.isNullOrEmpty()) {
        visibility = View.GONE
        text = ""
    } else {
        visibility = View.VISIBLE
        text = message
        visibleInvoke.invoke(this)
    }
}


fun TextView.setTextOrInvisible(message: CharSequence?) {
    visibility = if (message.isNullOrEmpty()) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
    text = message
}