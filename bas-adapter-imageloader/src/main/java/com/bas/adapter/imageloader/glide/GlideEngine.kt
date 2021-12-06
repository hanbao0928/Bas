package com.bas.adapter.imageloader.glide

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bas.adapter.imageloader.*
import com.bas.adapter.imageloader.DEBUG
import com.bas.adapter.imageloader.logw
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

/**
 * Created by Lucio on 2021/11/4.
 * 文档地址：https://muyangmin.github.io/glide-docs-cn/doc/getting-started.html
 * Github：https://github.com/bumptech/glide
 *
 *  注意：
 * 避免在程序库中使用 AppGlideModule:
 * https://muyangmin.github.io/glide-docs-cn/doc/configuration.html#%E9%81%BF%E5%85%8D%E5%9C%A8%E7%A8%8B%E5%BA%8F%E5%BA%93%E4%B8%AD%E4%BD%BF%E7%94%A8-appglidemodule
 *
 */
object GlideEngine : ImageLoaderEngine {

    private val centerCrop = CenterCrop()

    private val circleCrop = CircleCrop()

    private var isDefaultRequestOptionInit = false

    /**
     * 默认请求参数
     */
    lateinit var defaultRequestOptions: RequestOptions
        private set

    /**
     * 默认圆形请求参数
     */
    lateinit var defaultCircleRequestOptions: RequestOptions
        private set

    /**
     * 默认圆角参数
     */
    lateinit var defaultRoundedRequestOptions: RequestOptions
        private set

//    fun setDefaultRequestOptions(options: RequestOptions) {
//        defaultCircleRequestOptions = options.clone().also {
//            it.transform(CenterCrop(), CircleCrop())
//        }
//    }


    private inline fun ensureDefaultRequestOptions() {
        if (isDefaultRequestOptionInit)
            return
        setDefaultRequestOptions(RequestOptionsBas())
    }

    internal fun setDefaultRequestOptions(options: RequestOptions) {
        isDefaultRequestOptionInit = true
        this.defaultRequestOptions = options
        this.defaultCircleRequestOptions = options.clone().apply {
            transform(centerCrop, circleCrop)
        }
        this.defaultRoundedRequestOptions = options.clone().apply {
            transform(centerCrop, RoundedCorners(ImageLoader.defaultRoundedImageRadius))
        }
    }

    fun Request(imageView: ImageView): RequestManager {
        ensureDefaultRequestOptions()
        return Glide.with(imageView)
            .setDefaultRequestOptions(defaultRequestOptions)
    }

    fun CircleRequest(imageView: ImageView): RequestManager {
        ensureDefaultRequestOptions()
        return Glide.with(imageView)
            .setDefaultRequestOptions(defaultCircleRequestOptions)

    }

    fun RoundedRequest(imageView: ImageView): RequestManager {
        ensureDefaultRequestOptions()
        return Glide.with(imageView)
            .setDefaultRequestOptions(defaultRoundedRequestOptions)
    }

    /**
     * Clears disk cache.
     * This method should always be called on a background thread, since it is a blocking call.
     */
    override fun clearDiskCache(ctx: Context) {
        Glide.get(ctx).clearDiskCache()
    }

    /**
     * Clears as much memory as possible.
     * call this method must on Main Thread
     *
     * @see android.content.ComponentCallbacks.onLowMemory
     * @see android.content.ComponentCallbacks2.onLowMemory
     */
    override fun clearMemoryCache(ctx: Context) {
        Glide.get(ctx).clearMemory()
    }

    /**
     * 清除[view]上的图片加载请求
     */
    override fun clear(view: View) {
        Glide.with(view).clear(view)
    }

    override fun load(imageView: ImageView, url: String?) {
        Request(imageView)
            .load(url)
            .optionalCenterCrop()
            .into(imageView)
    }

    /**
     * 加载图片
     */
    override fun load(imageView: ImageView, url: String?, placeHolder: Int) {
        Request(imageView)
            .load(url)
            .placeholder(placeHolder)
//            .listener(object : RequestListener<Drawable> {
//
//                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    loge("Load failed，url=${url}",e)
//                    e?.logRootCauses(TAG)
//                    return false
//                }
//
//
//                override fun onResourceReady(
//                    resource: Drawable?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    logi("Load Success，isFirstResource=${isFirstResource} \nurl=${url}")
//                    return false
//                }
//
//            })
            .into(imageView)
    }

    override fun load(imageView: ImageView, url: String?, placeHolder: Drawable?) {
        Request(imageView)
            .load(url)
            .placeholder(placeHolder)
            .into(imageView)
    }

    override fun load(imageView: ImageView, url: String?, placeHolder: Int, errorPlaceHolder: Int) {
        Request(imageView)
            .load(url)
            .placeholder(placeHolder)
            .error(errorPlaceHolder)
            .into(imageView)
    }

    override fun load(
        imageView: ImageView,
        url: String?,
        placeHolder: Drawable?,
        errorPlaceHolder: Drawable?
    ) {
        Request(imageView)
            .load(url)
            .placeholder(placeHolder)
            .error(errorPlaceHolder)
            .into(imageView)
    }

    override fun load(
        imageView: ImageView,
        url: String?,
        placeHolder: Int,
        errorPlaceHolder: Drawable?
    ) {
        Request(imageView)
            .load(url)
            .placeholder(placeHolder)
            .error(errorPlaceHolder)
            .into(imageView)
    }

    override fun load(
        imageView: ImageView,
        url: String?,
        placeHolder: Drawable?,
        errorPlaceHolder: Int
    ) {
        Request(imageView)
            .load(url)
            .placeholder(placeHolder)
            .error(errorPlaceHolder)
            .into(imageView)
    }

    /**
     * @param roundingRadius 圆角半径，单位px
     */
    override fun loadRounded(imageView: ImageView, url: String?, roundingRadius: Int) {
        if (roundingRadius == ImageLoader.defaultRoundedImageRadius) {
            RoundedRequest(imageView)
                .load(url)
                .into(imageView)
        } else {
            Request(imageView)
                .load(url)
                .transform(centerCrop, RoundedCorners(roundingRadius))
                .into(imageView)
        }
    }

    override fun loadRounded(
        imageView: ImageView,
        url: String?,
        roundingRadius: Int,
        placeHolder: Int
    ) {
        if (roundingRadius == ImageLoader.defaultRoundedImageRadius) {
            RoundedRequest(imageView)
                .load(url)
                .placeholder(placeHolder)
                .into(imageView)
        } else {
            Request(imageView)
                .load(url)
                .placeholder(placeHolder)
                .transform(centerCrop, RoundedCorners(roundingRadius))
                .into(imageView)
        }
    }

    override fun loadRounded(
        imageView: ImageView,
        url: String?,
        roundingRadius: Int,
        placeHolder: Drawable?
    ) {
        if (roundingRadius == ImageLoader.defaultRoundedImageRadius) {
            RoundedRequest(imageView)
                .load(url)
                .placeholder(placeHolder)
                .into(imageView)
        } else {
            Request(imageView)
                .load(url)
                .placeholder(placeHolder)
                .transform(centerCrop, RoundedCorners(roundingRadius))
                .into(imageView)
        }
    }

    override fun loadRounded(
        imageView: ImageView,
        url: String?,
        roundingRadius: Int,
        placeHolder: Drawable?,
        error: Drawable?
    ) {
        if (roundingRadius == ImageLoader.defaultRoundedImageRadius) {
            RoundedRequest(imageView)
                .load(url)
                .placeholder(placeHolder)
                .error(error)
                .into(imageView)
        } else {
            Request(imageView)
                .load(url)
                .placeholder(placeHolder)
                .error(error)
                .transform(centerCrop, RoundedCorners(roundingRadius))
                .into(imageView)
        }
    }

    /**
     * 加载圆角图片
     * @param applyPlaceHolder  不建议使用该功能；请参考文档"Glide注意点"
     * 圆角转换是否应用到占位图，如果占位图本身是圆角，则设置该属性为false，避免重新转换占位图。
     * 该属性为true时加载占位图有一定的时间消耗可能会略微引起界面闪烁
     */
    override fun loadRounded(
        imageView: ImageView,
        url: String?,
        roundingRadius: Int,
        placeHolder: Int,
        applyPlaceHolder: Boolean
    ) {
        if (applyPlaceHolder) {
            if (DEBUG) {
                logw("在应用中包含必须在运行时做变换才能使用的图片资源是很不划算的，建议使用圆角占位图")
            }
            val placeHolderLoader = Glide.with(imageView)
                .load(placeHolder)
                .transform(centerCrop, RoundedCorners(roundingRadius))
            Request(imageView)
                .load(url)
                .thumbnail(placeHolderLoader)
                .transform(centerCrop, RoundedCorners(roundingRadius))
                .into(imageView)
        } else {
            loadRounded(imageView, url, roundingRadius, placeHolder)
        }
    }

    override fun loadRounded(
        imageView: ImageView,
        url: String?,
        roundingRadius: Int,
        placeHolder: Drawable?,
        applyPlaceHolder: Boolean
    ) {
        if (applyPlaceHolder && placeHolder != null) {
            if (DEBUG) {
                logw("在应用中包含必须在运行时做变换才能使用的图片资源是很不划算的，建议使用圆角占位图")
            }
            val placeHolderLoader = Glide.with(imageView)
                .load(placeHolder)
                .transform(centerCrop, RoundedCorners(roundingRadius))
            Request(imageView)
                .load(url)
                .thumbnail(placeHolderLoader)
                .transform(centerCrop, RoundedCorners(roundingRadius))
                .into(imageView)
        } else {
            loadRounded(imageView, url, roundingRadius, placeHolder)
        }
    }

    override fun loadCircle(imageView: ImageView, url: String?) {
        CircleRequest(imageView)
            .load(url)
            .into(imageView)
    }

    override fun loadCircle(imageView: ImageView, url: String?, placeHolder: Int) {
        CircleRequest(imageView)
            .load(url)
            .placeholder(placeHolder)
            .into(imageView)
    }

    override fun loadCircle(imageView: ImageView, url: String?, placeHolder: Drawable?) {
        CircleRequest(imageView)
            .load(url)
            .placeholder(placeHolder)
            .into(imageView)
    }

    /**
     * 加载圆形图片
     * @param applyPlaceHolder 圆形转换是否应用到占位图，如果占位图本身是圆形，则设置该属性为false，避免重新转换占位图。
     * 该属性为true时加载占位图有一定的时间消耗可能会略微引起界面闪烁
     */
    override fun loadCircle(
        imageView: ImageView,
        url: String?,
        placeHolder: Int,
        applyPlaceHolder: Boolean
    ) {
        if (applyPlaceHolder) {
            if (DEBUG) {
                logw("在应用中包含必须在运行时做变换才能使用的图片资源是很不划算的，建议使用圆形占位图")
            }
            val placeHolderLoader = Glide.with(imageView)
                .load(placeHolder)
                .transform(centerCrop, circleCrop)
            CircleRequest(imageView)
                .load(url)
                .thumbnail(placeHolderLoader)
                .into(imageView)
        } else {
            loadCircle(imageView, url, placeHolder)
        }
    }

    override fun loadCircle(
        imageView: ImageView,
        url: String?,
        placeHolder: Drawable?,
        applyPlaceHolder: Boolean
    ) {
        if (applyPlaceHolder && placeHolder != null) {
            if (DEBUG) {
                logw("在应用中包含必须在运行时做变换才能使用的图片资源是很不划算的，建议使用圆形占位图")
            }
            val placeHolderLoader = Glide.with(imageView)
                .load(placeHolder)
                .transform(centerCrop, circleCrop)
            CircleRequest(imageView)
                .load(url)
                .thumbnail(placeHolderLoader)
                .into(imageView)
        } else {
            loadCircle(imageView, url, placeHolder)
        }
    }

}