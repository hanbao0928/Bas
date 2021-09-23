
##---------------bas-adapter-mediaplayer ProGuard
-keepattributes *Annotation*

# for kotlin
-keep class kotlin.** { *; }
-keep @kotlin.Metadata class *
-keepclasseswithmembers @kotlin.Metadata class * { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}

# for bas-adapter-mediaplayer
-keep public class com.bas.adapter.mediaplayer.**{
    public *;
    protected * ;
}

##---------------bas-adapter-mediaplayer ProGuard END

##---------------七牛播放器混淆规则 https://developer.qiniu.com/pili/1210/the-android-client-sdk#5
-keep class com.pili.pldroid.player.** { *; }
-keep class com.qiniu.qplayer.mediaEngine.MediaPlayer{*;}
##---------------七牛播放器混淆规则 - END

##---------------腾讯云直播播放器 混淆规则 https://cloud.tencent.com/document/product/1449/56987
-keep class com.tencent.** { *; }
##---------------腾讯云直播播放器 混淆规则 -END

