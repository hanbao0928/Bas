package com.bas.adapter.imageloader.glide

import android.util.Log
import com.bas.adapter.imageloader.ImageLoader
import com.bumptech.glide.load.engine.DiskCacheStrategy


/**
 * Created by Lucio on 2021/11/4.
 */

/**
 * 创建请求参数
 */
fun RequestOptionsBas(): com.bumptech.glide.request.RequestOptions {
    Log.d("GlideEngineModule","RequestOptionsBas")
    return com.bumptech.glide.request.RequestOptions()
        .skipMemoryCache(ImageLoader.config.isMemoryCacheEnabled)
        .diskCacheStrategy(if(!ImageLoader.config.isDiskCacheEnabled) DiskCacheStrategy.NONE else  DiskCacheStrategy.AUTOMATIC)
        .format(
            com.bumptech.glide.load.DecodeFormat.PREFER_RGB_565
        )
}