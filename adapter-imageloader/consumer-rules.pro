## for adapter-imageloader
-keep public class bas.droid.adapter.imageloader.**{
    public *;
    protected * ;
}

##---------------BEGIN: Glide  ----------
# 来源: https://github.com/bumptech/glide#proguard
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

# for DexGuard only
# -keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# target api < 27添加下面混淆规则 来源：http://bumptech.github.io/glide/doc/download-setup.html#proguard
#-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder


# https://muyangmin.github.io/glide-docs-cn/doc/configuration.html
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl

##---------------END: Glide  ----------