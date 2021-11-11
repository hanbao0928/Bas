package com.bas.adapter.imageloader.glide

import android.content.Context
import android.util.Log
import com.bas.adapter.imageloader.ImageLoader
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.engine.cache.DiskCache
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions


/**
 * Created by Lucio on 2021/11/4.
 */
abstract class GlideEngineModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        applyDiskCacheConfig(context, builder)
        applyDefaultRequestOptionsConfig(context, builder)
    }

    protected open fun applyDefaultRequestOptionsConfig(context: Context, builder: GlideBuilder) {
        Log.d("GlideEngineModule", "applyDefaultRequestOptionsConfig")
        builder.setDefaultRequestOptions(createDefaultRequestOptions())
    }

    protected open fun createDefaultRequestOptions(): RequestOptions {
        return RequestOptionsBas()
    }

    protected open fun applyDiskCacheConfig(context: Context, builder: GlideBuilder) {
        val dirName = ImageLoader.config.diskCacheFolderName
        if (dirName.isNullOrEmpty()) {
            builder.setDiskCache(ExternalPreferredCacheDiskCacheFactory(context))
        } else {
            builder.setDiskCache(
                ExternalPreferredCacheDiskCacheFactory(
                    context, dirName,
                    ImageLoader.DEFAULT_DISK_CACHE_SIZE
                )
            )
        }
    }

}