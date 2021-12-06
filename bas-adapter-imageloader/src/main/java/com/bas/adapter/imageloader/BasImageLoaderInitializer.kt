//package com.bas.adapter.imageloader
//
//import android.content.Context
//import androidx.startup.Initializer
//
///**
// * Created by Lucio on 2020/12/16.
// */
//@Deprecated("考虑此方式并不是很需要，毕竟使用者可能考虑性能、考虑多进程等因素来调用，所以便不再主动在AndroidManifest中注册" +
//        "如果需要，参照本库的AndroidManifest中注释代码添加即可")
//class BasImageLoaderInitializer : Initializer<ImageLoader> {
//    override fun create(context: Context): ImageLoader {
//        if(DEBUG){
//            logi("BasImageLoaderInitializer invoke.")
//        }
//        ImageLoader.init(context)
//        return ImageLoader
//    }
//
//    override fun dependencies(): List<Class<out Initializer<*>>> {
//        return emptyList()
//    }
//
//}