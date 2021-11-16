/**
 * Created by Lucio on 2021/11/12.
 */
package com.bas.core.android.binding

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bas.adapter.imageloader.*


/**
 * 默认圆角半径
 */
internal var defaultRoundingRadius: Int = 12

private inline val Int?.orDefaultRoundingRadius get() = this ?: defaultRoundingRadius

/**
 * 加载图片（不使用占位图）
 */
@BindingAdapter("bindImage0")
fun bindImage0(
    view: ImageView,
    imageUrl: String?
) {
    view.load0(imageUrl)
}

/**
 * 加载图片（并显示占位图：如果未设置占位图，则使用默认占位图）
 */
@BindingAdapter(value = ["bindImage", "placeholder", "error"], requireAll = false)
fun bindImage(
    view: ImageView,
    imageUrl: String?,
    placeHolder: Drawable?,
    error: Drawable?
) {
    Log.d("BindImage", "执行Bas BindImage")
    if (placeHolder == null) {
        view.load(imageUrl, defaultImageResBas, error)
    } else {
        view.load(imageUrl, placeHolder, error)
    }
}

@BindingAdapter(
    value = ["bindRoundedImage0", "placeHolder", "roundingRadius", "useRoundedStrict"],
    requireAll = false
)
fun bindRoundedImage0(
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
        if (placeHolder == null) {
            view.loadRoundedStrict(
                url,
                roundingRadius.orDefaultRoundingRadius,
                defaultRoundedImageResBas
            )
        } else {
            view.loadRoundedStrict(url, roundingRadius.orDefaultRoundingRadius, placeHolder)
        }
    } else {
        if (placeHolder == null) {
            view.loadRounded(url, roundingRadius.orDefaultRoundingRadius, defaultRoundedImageResBas)
        } else {
            view.loadRounded(url, roundingRadius.orDefaultRoundingRadius, placeHolder)
        }
    }
}

/**
 * @param useCircleStrict 是否对占位图应用圆形转换，true应用，false则不应用；建议不使用该功能，提供对应效果的占位图更好
 */
@BindingAdapter(value = ["bindCircleImage0", "placeHolder", "useCircleStrict"], requireAll = false)
fun bindCircleImage0(
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
        if (placeHolder == null) {
            view.loadCircleStrict(url, defaultCircleImageResBas)
        } else {
            view.loadCircleStrict(url, placeHolder)
        }
    } else {
        if (placeHolder == null) {
            view.loadCircle(url, defaultCircleImageResBas)
        } else {
            view.loadCircle(url, placeHolder)
        }
    }
}