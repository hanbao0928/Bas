package com.bas.adapter.imageloader

import com.bas.adapter.imageloader.glide.GlideEngine

internal var engine: ImageLoaderEngine = GlideEngine
    private set

/**
 * Created by Lucio on 2021/11/4.
 */
object ImageLoader : ImageLoaderEngine by engine {

    @JvmStatic
    internal var config: Config = Config()
        private set

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
     * 用于建议的默认disk cache 目录名字
     */
    const val DEFAULT_DISK_CACHE_FOLDER_NAME = "bas_image_loader_disk_cache"

    /**
     * 用于建议的默认disk cache 大小
     */
    const val DEFAULT_DISK_CACHE_SIZE =   250 * 1024 * 1024L

    class Config internal constructor() {

        /**
         * 是否启用 disk cache
         */
        @JvmField
        var isDiskCacheEnabled: Boolean = true


        /**
         * 是否启用memory ache
         */
        @JvmField
        var isMemoryCacheEnabled: Boolean = true

        /**
         * disk 缓存目录名字
         */
        @JvmField
        var diskCacheFolderName: String? = null

        class Builder() {

            private var isDiskCacheEnabled = true
            private var isMemoryCacheEnabled: Boolean = true
            private var diskCacheFolderName: String? = null

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

            fun build(): Config {
                return Config().also {
                    it.isDiskCacheEnabled = this.isDiskCacheEnabled
                    it.isMemoryCacheEnabled = this.isMemoryCacheEnabled
                    it.diskCacheFolderName = this.diskCacheFolderName
                }
            }
        }
    }

}