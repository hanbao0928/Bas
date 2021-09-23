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

# bas-adapter-mediaplayer混淆规则
-keep public class com.bas.adapter.mediaplayer.**{
    public *;
    protected * ;
}

# 七牛播放器混淆规则 https://developer.qiniu.com/pili/1210/the-android-client-sdk#5
-keep class com.pili.pldroid.player.** { *; }
-keep class com.qiniu.qplayer.mediaEngine.MediaPlayer{*;}
# 七牛播放器混淆规则 - END

# 腾讯云直播播放器 混淆规则 https://cloud.tencent.com/document/product/1449/56987
-keep class com.tencent.** { *; }
# 腾讯云直播播放器 混淆规则 -END

