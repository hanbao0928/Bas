package bas.droid.adapter.imageloader.glide

import android.content.Context
import bas.droid.adapter.imageloader.ImageLoaderAdapter
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

/**
 * Created by Lucio on 2021/11/4.
如果您也计划使用`Glide`作为图片加载的实现，并且通过`ImageLoader#setConfig`修改了默认配置信息，
或者您计划使用Glide的Generate Api，则您应该在您的app module中增加如下配置：
```
@GlideModule
public class MyAppGlideModule extends GlideEngineModule {

}

```
如果您的工程中已经包含`@GlideModule`的定义，则您可以考虑将父类修改为`GlideEngineModule`，
或者将`GlideEngineModule`内的实现复制到您的`@GlideModule`类中，
`GlideEngineModule`内实现的逻辑只是为了确保`ImageLoader#setConfig`设置的配置项能够生效。
 */
abstract class GlideEngineModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        applyDiskCacheConfig(context, builder)
        applyDefaultRequestOptionsConfig(context, builder)
    }

    protected open fun applyDefaultRequestOptionsConfig(context: Context, builder: GlideBuilder) {
        builder.setDefaultRequestOptions(createDefaultRequestOptions().also {
            GlideEngine.setDefaultRequestOptions(it)
        })
    }

    protected open fun createDefaultRequestOptions(): RequestOptions {
        return RequestOptionsAIL()
    }

    protected open fun applyDiskCacheConfig(context: Context, builder: GlideBuilder) {
        val dirName = ImageLoaderAdapter.config.diskCacheFolderName
        if (dirName.isNullOrEmpty()) {
            builder.setDiskCache(ExternalPreferredCacheDiskCacheFactory(context))
        } else {
            builder.setDiskCache(
                ExternalPreferredCacheDiskCacheFactory(
                    context, dirName,
                    ImageLoaderAdapter.DEFAULT_DISK_CACHE_SIZE
                )
            )
        }
    }

}