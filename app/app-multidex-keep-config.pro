
## 主入口类
-keep class * extends android.app.Application

## Kotlin
-keep class kotlin.** { *; }
## Kotlin End

# bugly & customactivityoncrash
-keep class com.tencent.bugly.**{*;}
-keep class cat.ereza.customactivityoncrash.**{*;}

## MultiDexCompat
-keep class com.bas.**{*;}



