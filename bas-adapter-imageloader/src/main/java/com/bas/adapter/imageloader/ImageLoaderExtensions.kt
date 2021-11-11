package com.bas.adapter.imageloader

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes

/**
 * Created by Lucio on 2021/11/4.
 */


inline fun ImageView.load(url: String?) {
    ImageLoader.load(this, url)
}

inline fun ImageView.load(url: String?, @DrawableRes placeHolder: Int) {
    ImageLoader.load(this, url, placeHolder)
}

inline fun ImageView.load(url: String?, placeHolder: Drawable) {
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
    placeHolder: Drawable,
    errorPlaceHolder: Drawable
) {
    ImageLoader.load(this, url, placeHolder, errorPlaceHolder)
}

inline fun ImageView.load(
    url: String?,
    @DrawableRes placeHolder: Int,
    errorPlaceHolder: Drawable
) {
    ImageLoader.load(this, url, placeHolder, errorPlaceHolder)
}

inline fun ImageView.load(
    url: String?,
    placeHolder: Drawable,
    @DrawableRes errorPlaceHolder: Int
) {
    ImageLoader.load(this, url, placeHolder, errorPlaceHolder)
}

/**
 * @param roundingRadius 圆角半径，单位px
 */
inline fun ImageView.loadRounded(
    url: String?,
    roundingRadius: Int
) {
    ImageLoader.loadRounded(this, url, roundingRadius)
}

inline fun ImageView.loadRounded(
    url: String?,
    roundingRadius: Int,
    @DrawableRes placeHolder: Int
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

inline fun ImageView.loadCircle(
    url: String?
) {
    ImageLoader.loadCircle(this, url)
}

inline fun ImageView.loadCircle(
    url: String?,
    @DrawableRes placeHolder: Int
){
    ImageLoader.loadCircle(this, url,placeHolder)
}

inline fun ImageView.loadCircle(
    url: String?,
    placeHolder: Drawable?
){
    ImageLoader.loadCircle(this, url,placeHolder)
}


/**
 * 加载圆形图片,并对占位图应用圆形转换
 */
@Deprecated("不建议在占位图上使用转换效果，最好使用圆角效果的占位图")
inline fun ImageView.loadCircleStrict(
    url: String?,
    @DrawableRes placeHolder: Int
) {
    ImageLoader.loadCircle(this, url,  placeHolder, true)
}

/**
 * 加载圆形图片,并对占位图应用圆形转换
 */
@Deprecated("不建议在占位图上使用转换效果，最好使用圆角效果的占位图")
inline fun ImageView.loadCircleStrict(
    url: String?,
    placeHolder: Drawable?
) {
    ImageLoader.loadCircle(this, url,  placeHolder, true)
}
