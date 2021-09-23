# bas-adapter-mediaplayer混淆规则
-keep public class com.bas.adapter.mediaplayer.**{
    public *;
    protected * ;
}

# 七牛播放器混淆规则
-keep class com.pili.pldroid.player.** { *; }
-keep class com.qiniu.qplayer.mediaEngine.MediaPlayer{*;}
# 七牛播放器混淆规则 - END

# 腾讯云直播播放器 混淆规则
-keep class com.tencent.** { *; }
# 腾讯云直播播放器 混淆规则 -END