package com.bas.adapter.imageloader

import android.content.Context
import androidx.startup.Initializer

/**
 * Created by Lucio on 2020/12/16.
 */
class BasImageLoaderInitializer : Initializer<ImageLoader> {
    override fun create(context: Context): ImageLoader {
        if(DEBUG){
            logi("BasImageLoaderInitializer invoke.")
        }
        ImageLoader.init(context)
        return ImageLoader
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

}