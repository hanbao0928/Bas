# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

## for bas-adapter-imageloader
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