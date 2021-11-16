package com.bas.adapter.imageloader

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes


/**
 * Created by Lucio on 2021/11/4.
 */
object ImageLoader : ImageLoaderEngine by engine {

    @JvmStatic
    var config: Config = Config()
        private set

    /**
     * 默认图片圆角半径
     * [Config]中未提供该属性，主要是考虑通过代码设置的值无法写入默认的圆角图片资源（shape xml）中，
     * 所以修改该值可以通过提供 资源<dimen name="image_rounded_radius_bas"></dimen>覆盖。
     */
    var defaultRoundedImageRadius: Int = 12
        private set

    fun init(ctx: Context) {
        defaultRoundedImageRadius =
            ctx.resources.getDimensionPixelSize(R.dimen.image_rounded_radius_bas)

        if (DEBUG) {
            logi(
                "BasImageLoader init:\n" +
                        "defaultImageRoundingRadius=$defaultRoundedImageRadius"
            )
        }
    }

    /**
     * 设置配置
     */
    @JvmStatic
    fun setConfig(config: Config) {
        this.config = config
    }

    /**
     * 设置加在内核
     */
    @JvmStatic
    fun setEngine(imageLoaderEngine: ImageLoaderEngine) {
        engine = imageLoaderEngine
    }

    /**
     * 加载圆角图片（使用默认圆角）
     */
    @JvmStatic
    fun loadRounded(imageView: ImageView, url: String?) {
        loadRounded(imageView, url, defaultRoundedImageRadius)
    }

    /**
     * 用于建议的默认disk cache 目录名字
     */
    const val DEFAULT_DISK_CACHE_FOLDER_NAME = "bas_image_loader_disk_cache"

    /**
     * 用于建议的默认disk cache 大小
     */
    const val DEFAULT_DISK_CACHE_SIZE = 250 * 1024 * 1024L

    class Config internal constructor() {

        /**
         * 是否启用 disk cache
         */
        @JvmField
        var isDiskCacheEnabled: Boolean = true

        /**
         * 是否启用 memory ache
         */
        @JvmField
        var isMemoryCacheEnabled: Boolean = true

        /**
         * 默认图片资源
         */
        @JvmField
        var defaultImageRes: Int = R.drawable.image_placeholder_bas

        /**
         * 默认圆角图片
         */
        @JvmField
        var defaultRoundedImageRes: Int = R.drawable.rounded_image_placeholder_bas

        /**
         * 默认圆形图片
         */
        @JvmField
        var defaultCircleImageRes: Int = R.drawable.circle_image_placeholder_bas

        /**
         * disk 缓存目录名字,如果为空，则使用对应Engine的默认目录名字；
         * Glide默认配置的文件目录名为[com.bumptech.glide.load.engine.cache.DiskCache.Factory.DEFAULT_DISK_CACHE_DIR]
         */
        @JvmField
        var diskCacheFolderName: String? = null

        class Builder() {
            private var isDiskCacheEnabled = true
            private var isMemoryCacheEnabled: Boolean = true
            private var diskCacheFolderName: String? = null
            private var defaultImageRes: Int = R.drawable.image_placeholder_bas
            private var defaultRoundedImageRes: Int = R.drawable.rounded_image_placeholder_bas
            private var defaultCircleImageRes: Int = R.drawable.circle_image_placeholder_bas

            /**
             * 是否启用Disk缓存
             */
            fun setDiskCacheEnabled(isEnable: Boolean): Builder {
                this.isDiskCacheEnabled = isEnable
                return this
            }

            /**
             * 是否启用Memory缓存
             */
            fun setMemoryCacheEnabled(isEnable: Boolean): Builder {
                this.isMemoryCacheEnabled = isEnable
                return this
            }

            /**
             * 设置Disk 缓存目录名字
             */
            fun setDiskCacheFolderName(name: String?): Builder {
                this.diskCacheFolderName = name
                return this
            }

            fun setDefaultImage(@DrawableRes resId: Int): Builder {
                this.defaultImageRes = resId
                return this
            }

            fun setDefaultRoundedImage(@DrawableRes resId: Int): Builder {
                this.defaultRoundedImageRes = resId
                return this
            }

            fun setDefaultCircleImage(@DrawableRes resId: Int): Builder {
                this.defaultCircleImageRes = resId
                return this
            }

            fun build(): Config {
                return Config().also {
                    it.isDiskCacheEnabled = this.isDiskCacheEnabled
                    it.isMemoryCacheEnabled = this.isMemoryCacheEnabled
                    it.diskCacheFolderName = this.diskCacheFolderName
                    it.defaultImageRes = this.defaultImageRes
                    it.defaultRoundedImageRes = this.defaultRoundedImageRes
                    it.defaultCircleImageRes = this.defaultCircleImageRes
                }
            }
        }
    }

}