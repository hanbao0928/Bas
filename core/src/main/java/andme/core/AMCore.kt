@file:JvmName("AMCore")
@file:JvmMultifileClass

/**
 * Created by Lucio on 2020-11-09.
 */
package andme.core

import andme.integration.media.MediaStore
import bas.lib.core.lang.orDefault

//媒体相关功能支持器懒加载函数
var mediaStoreCreator: (() -> MediaStore)? = null

/**
 * 媒体相关功能支持器：用于媒体操作的统一管理
 */
var mediaStoreAM: MediaStore
    get() {
        return mMediaStore.orDefault {
            mediaStoreCreator?.invoke()
        }.orDefault {
            val clzName = Class.forName("$INTEGRATION_PKG_NAME.media.MediaStoreImpl")
            clzName.getDeclaredConstructor().newInstance() as MediaStore
        }
    }
    set(value) {
        mMediaStore = value
    }





