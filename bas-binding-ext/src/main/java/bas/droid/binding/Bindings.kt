package bas.droid.binding

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * Created by Lucio on 2021/11/11.
 */

@BindingAdapter("bindTextOrGone")
fun bindTextOrGone(view: TextView, oldValue: CharSequence?, newValue: CharSequence?) {
    if (oldValue == newValue)
        return

    if (newValue.isNullOrEmpty()) {
        view.visibility = View.GONE
        view.text = ""
    } else {
        view.visibility = View.VISIBLE
        view.text = newValue
    }
}

@BindingAdapter("bindTextOrInvisible")
fun bindTextOrInvisible(view: TextView, oldValue: CharSequence?, newValue: CharSequence?) {
    if (oldValue == newValue)
        return
    if (newValue.isNullOrEmpty()) {
        view.visibility = View.INVISIBLE
        view.text = ""
    } else {
        view.visibility = View.VISIBLE
        view.text = newValue
    }
}

@BindingAdapter("bindGoneUnless")
fun bindGoneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("bindInvisibleUnless")
fun bindInvisibleUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}