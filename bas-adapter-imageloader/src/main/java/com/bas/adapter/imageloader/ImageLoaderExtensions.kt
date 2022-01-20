/**
 * Created by Lucio on 2021/11/4.
 */
package com.bas.adapter.imageloader

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes

/**
 * 默认图片资源
 */
@get:DrawableRes
inline val defaultImageResBas: Int get() = ImageLoader.config.defaultImageRes

/**
 * 默认圆角图片
 */
@get:DrawableRes
inline val defaultRoundedImageResBas: Int get() = ImageLoader.config.defaultRoundedImageRes

/**
 * 默认圆形图片
 */
@get:DrawableRes
inline val defaultCircleImageResBas: Int get() = ImageLoader.config.defaultCircleImageRes

/**
 * 图片圆角半径
 */
@get:DrawableRes
inline val defaultRoundedImageRadius: Int get() = ImageLoader.defaultRoundedImageRadius

/**
 * 加载图片（不会使用默认占位图）
 */
inline fun ImageView.load0(url: String?) {
    ImageLoader.load(this, url)
}

/**
 * 使用占位图设置加载图片
 */
@JvmOverloads
inline fun ImageView.load(url: String?, @DrawableRes placeHolder: Int = defaultImageResBas) {
    ImageLoader.load(this, url, placeHolder)
}

inline fun ImageView.load(url: String?, placeHolder: Drawable?) {
    ImageLoader.load(this, url, placeHolder)
}

inline fun ImageView.load(
    url: String?,
    @DrawableRes placeHolder: Int,
    @DrawableRes errorPlaceHolder: Int
) {
    ImageLoader.load(this, url, placeHolder, errorPlaceHolder)
}

inline fun ImageView.load(
    url: String?,
    placeHolder: Drawable?,
    errorPlaceHolder: Drawable?
) {
    ImageLoader.load(this, url, placeHolder, errorPlaceHolder)
}

inline fun ImageView.load(
    url: String?,
    @DrawableRes placeHolder: Int,
    errorPlaceHolder: Drawable?
) {
    ImageLoader.load(this, url, placeHolder, errorPlaceHolder)
}

inline fun ImageView.load(
    url: String?,
    placeHolder: Drawable?,
    @DrawableRes errorPlaceHolder: Int
) {
    ImageLoader.load(this, url, placeHolder, errorPlaceHolder)
}

/**
 * 加载圆角图片（不会使用默认占位图）
 * @param roundingRadius 圆角半径，单位px
 */
inline fun ImageView.loadRounded0(
    url: String?,
    roundingRadius: Int
) {
    ImageLoader.loadRounded(this, url, roundingRadius)
}

/**
 * 加载圆角图片（会使用默认圆角图片作为占位图）
 * @param roundingRadius 圆角半径，单位px
 */
@JvmOverloads
inline fun ImageView.loadRounded(
    url: String?,
    roundingRadius: Int = defaultRoundedImageRadius,
    @DrawableRes placeHolder: Int = defaultRoundedImageResBas
) {
    ImageLoader.loadRounded(this, url, roundingRadius, placeHolder)
}

inline fun ImageView.loadRounded(
    url: String?,
    roundingRadius: Int,
    placeHolder: Drawable?
) {
    ImageLoader.loadRounded(this, url, roundingRadius, placeHolder)
}

inline fun ImageView.loadRounded(
    url: String?,
    roundingRadius: Int,
    placeHolder: Drawable?,
    error:Drawable?
) {
    ImageLoader.loadRounded(this, url, roundingRadius, placeHolder,error)
}


/**
 * 加载圆角图片,并对占位图应用圆角
 */
@Deprecated("不建议在占位图上使用转换效果，最好使用圆角效果的占位图")
inline fun ImageView.loadRoundedStrict(
    url: String?,
    roundingRadius: Int,
    @DrawableRes placeHolder: Int
) {
    ImageLoader.loadRounded(this, url, roundingRadius, placeHolder, true)
}

/**
 * 加载圆角图片,并对占位图应用圆角
 */
@Deprecated("不建议在占位图上使用转换效果，最好使用圆角效果的占位图")
inline fun ImageView.loadRoundedStrict(
    url: String?,
    roundingRadius: Int,
    placeHolder: Drawable?
) {
    ImageLoader.loadRounded(this, url, roundingRadius, placeHolder, true)
}


inline fun ImageView.loadCircle0(
    url: String?
) {
    ImageLoader.loadCircle(this, url)
}

@JvmOverloads
inline fun ImageView.loadCircle(
    url: String?,
    @DrawableRes placeHolder: Int = defaultCircleImageResBas
) {
    ImageLoader.loadCircle(this, url, placeHolder)
}

inline fun ImageView.loadCircle(
    url: String?,
    placeHolder: Drawable?
) {
    ImageLoader.loadCircle(this, url, placeHolder)
}

/**
 * 加载圆形图片,并对占位图应用圆形转换
 */
@Deprecated("不建议在占位图上使用转换效果，最好使用圆角效果的占位图")
inline fun ImageView.loadCircleStrict(
    url: String?,
    @DrawableRes placeHolder: Int
) {
    ImageLoader.loadCircle(this, url, placeHolder, true)
}

/**
 * 加载圆形图片,并对占位图应用圆形转换
 */
@Deprecated("不建议在占位图上使用转换效果，最好使用圆角效果的占位图")
inline fun ImageView.loadCircleStrict(
    url: String?,
    placeHolder: Drawable?
) {
    ImageLoader.loadCircle(this, url, placeHolder, true)
}
