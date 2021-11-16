/**
 * Created by Lucio on 2021/11/4.
 */
package com.bas.adapter.imageloader.glide

import com.bas.adapter.imageloader.DEBUG
import com.bas.adapter.imageloader.ImageLoader
import com.bas.adapter.imageloader.logi
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
//
///**
// * 默认请求参数
// */
//var defaultRequestOptions = RequestOptionsBas()
//    set(value) {
//        field = value
//        defaultCircleRequestOptions = value.clone().also {
//            it.transform(CenterCrop(), CircleCrop())
//        }
//    }
//
///**
// * 默认圆形请求参数
// */
//var defaultCircleRequestOptions = defaultRequestOptions.clone().also {
//    it.transform(CenterCrop(), CircleCrop())
//}

/**
 * 创建请求参数
 */
fun RequestOptionsBas(): com.bumptech.glide.request.RequestOptions {
    if(DEBUG){
        logi("create RequestOptionsBas")
    }
    return com.bumptech.glide.request.RequestOptions()
        .skipMemoryCache(ImageLoader.config.isMemoryCacheEnabled)
        .diskCacheStrategy(if (!ImageLoader.config.isDiskCacheEnabled) DiskCacheStrategy.NONE else DiskCacheStrategy.AUTOMATIC)
        .format(
            com.bumptech.glide.load.DecodeFormat.PREFER_RGB_565
        )
}