package com.bas.adapter.imageloader

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread

/**
 * Created by Lucio on 2021/11/4.
 */
interface ImageLoaderEngine {

    /**
     * Clears disk cache.
     * This method should always be called on a background thread, since it is a blocking call.
     */
    @WorkerThread
    fun clearDiskCache(ctx: Context)

    /**
     * Clears as much memory as possible.
     * call this method must on Main Thread
     *
     * @see android.content.ComponentCallbacks.onLowMemory
     * @see android.content.ComponentCallbacks2.onLowMemory
     */
    @MainThread
    fun clearMemoryCache(ctx: Context)

    /**
     * 清除[view]上的图片加载请求
     */
    fun clear(view: View)

    fun load(imageView: ImageView, url: String?)

    fun load(imageView: ImageView, url: String?, @DrawableRes placeHolder: Int)

    fun load(imageView: ImageView, url: String?,placeHolder: Drawable?)

    fun load(
        imageView: ImageView,
        url: String?,
        @DrawableRes placeHolder: Int,
        @DrawableRes errorPlaceHolder: Int
    )

    fun load(imageView: ImageView, url: String?, placeHolder: Drawable?, errorPlaceHolder: Drawable?)

    fun load(
        imageView: ImageView,
        url: String?,
        @DrawableRes placeHolder: Int,
        errorPlaceHolder: Drawable?
    )

    fun load(
        imageView: ImageView,
        url: String?,
        placeHolder: Drawable?,
        @DrawableRes errorPlaceHolder: Int
    )

    /**
     * @param roundingRadius 圆角半径，单位px
     */
    fun loadRounded(
        imageView: ImageView,
        url: String?,
        roundingRadius: Int
    )

    fun loadRounded(
        imageView: ImageView,
        url: String?,
        roundingRadius: Int,
        @DrawableRes placeHolder: Int
    )

    fun loadRounded(
        imageView: ImageView,
        url: String?,
        roundingRadius: Int,
        placeHolder: Drawable?
    )

    fun loadRounded(
        imageView: ImageView,
        url: String?,
        roundingRadius: Int,
        placeHolder: Drawable?,
        error:Drawable?
    )

    /**
     * 加载圆角图片
     * @param applyPlaceHolder 圆角转换是否应用到占位图，如果占位图本身是圆角，则设置该属性为false，避免重新转换占位图。
     * 该属性为true时加载占位图有一定的时间消耗可能会略微引起界面闪烁
     * @note 官方并不建议在占位图上使用变幻：https://muyangmin.github.io/glide-docs-cn/doc/placeholders.html
     * 变换是否会被应用到占位符上？
     * No。Transformation仅被应用于被请求的资源，而不会对任何占位符使用。
     * 在应用中包含必须在运行时做变换才能使用的图片资源是很不划算的。相反，在应用中包含一个确切符合尺寸和形状要求的资源版本几乎总是一个更好的办法。假如你正在加载圆形图片，你可能希望在你的应用中包含圆形的占位符。另外你也可以考虑自定义一个View来剪裁(clip)你的占位符，而达到你想要的变换效果。
     */
    fun loadRounded(
        imageView: ImageView,
        url: String?,
        roundingRadius: Int,
        @DrawableRes placeHolder: Int,
        applyPlaceHolder: Boolean
    )

    fun loadRounded(
        imageView: ImageView,
        url: String?,
        roundingRadius: Int,
        placeHolder: Drawable?,
        applyPlaceHolder: Boolean
    )

    fun loadCircle(
        imageView: ImageView,
        url: String?
    )

    fun loadCircle(
        imageView: ImageView,
        url: String?,
        @DrawableRes placeHolder: Int
    )

    fun loadCircle(
        imageView: ImageView,
        url: String?,
        placeHolder : Drawable?
    )

    /**
     * 加载圆形图片
     * @param applyPlaceHolder 圆形转换是否应用到占位图，如果占位图本身是圆形，则设置该属性为false，避免重新转换占位图。
     * 该属性为true时加载占位图有一定的时间消耗可能会略微引起界面闪烁
     * @note 官方并不建议在占位图上使用变幻：https://muyangmin.github.io/glide-docs-cn/doc/placeholders.html
     * 变换是否会被应用到占位符上？
     * No。Transformation仅被应用于被请求的资源，而不会对任何占位符使用。
     * 在应用中包含必须在运行时做变换才能使用的图片资源是很不划算的。相反，在应用中包含一个确切符合尺寸和形状要求的资源版本几乎总是一个更好的办法。假如你正在加载圆形图片，你可能希望在你的应用中包含圆形的占位符。另外你也可以考虑自定义一个View来剪裁(clip)你的占位符，而达到你想要的变换效果。
     */
    fun loadCircle(
        imageView: ImageView,
        url: String?,
        @DrawableRes placeHolder: Int,
        applyPlaceHolder: Boolean
    )

    fun loadCircle(
        imageView: ImageView,
        url: String?,
        placeHolder: Drawable?,
        applyPlaceHolder: Boolean
    )
}