package com.bas.core.android.binding

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bas.adapter.imageloader.*

/**
 * Created by Lucio on 2021/11/11.
 */

/**
 * 默认圆角半径
 */
private var _defaultRoundingRadius: Int = 12

private inline val Int?.orDefaultRoundingRadius get() = this ?: _defaultRoundingRadius

fun initBindings(defaultRoundingRadius: Int) {
    _defaultRoundingRadius = defaultRoundingRadius
}

@BindingAdapter(value = ["bindImage", "placeHolder"], requireAll = false)
fun bindImage(view: ImageView, imageUrl: String?, placeHolder: Drawable?) {
    view.load(imageUrl, placeHolder)
}

/**
 * @param useRoundedStrict 是否对占位图应用圆角，true应用，false则不应用；建议不使用该功能，提供对应效果的占位图更好
 */
@BindingAdapter(
    value = ["bindRoundedImage", "placeHolder", "roundingRadius", "useRoundedStrict"],
    requireAll = false
)
fun bindRoundedImage(
    view: ImageView,
    url: String?,
    placeHolder: Drawable?,
    roundingRadius: Int?,
    useRoundedStrict: Boolean?
) {
    if (useRoundedStrict == true) {
        view.loadRoundedStrict(url, roundingRadius.orDefaultRoundingRadius, placeHolder)
    } else {
        view.loadRounded(url, roundingRadius.orDefaultRoundingRadius, placeHolder)
    }
}

/**
 * @param useCircleStrict 是否对占位图应用圆形转换，true应用，false则不应用；建议不使用该功能，提供对应效果的占位图更好
 */
@BindingAdapter(value = ["bindCircleImage", "placeHolder", "useCircleStrict"], requireAll = false)
fun bindCircleImage(
    view: ImageView,
    url: String?,
    placeHolder: Drawable?,
    useCircleStrict: Boolean?
) {
    if (useCircleStrict == true) {
        view.loadCircleStrict(url, placeHolder)
    } else {
        view.loadCircle(url, placeHolder)
    }
}


@BindingAdapter("bindTextOrGone")
fun bindTextOrGone(view: TextView, message: CharSequence?) {
    if (message.isNullOrEmpty()) {
        view.visibility = View.GONE
        view.text = ""
    } else {
        view.visibility = View.VISIBLE
        view.text = message
    }
}

@BindingAdapter("bindTextOrInvisible")
fun bindTextOrInvisible(view: TextView, message: CharSequence?) {
    if (message.isNullOrEmpty()) {
        view.visibility = View.INVISIBLE
        view.text = ""
    } else {
        view.visibility = View.VISIBLE
        view.text = message
    }
}

@BindingAdapter("bindVisibleOrGone")
fun bindVisibleOrGone(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}