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