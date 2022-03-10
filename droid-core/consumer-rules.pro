##---------------Bas ProGuard
# for bas-core
-keep public class com.bas.core.**{
    public *;
    protected * ;
}

# 保持注解属性
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

##---------------Bas ProGuard END



## Gson ProGuard 来源：https://github.com/google/gson/blob/master/examples/android-proguard-example/proguard.cfg
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

##---------------End: proguard configuration for Gson  ----------


##---------------FastJson Proguard 参考：https://blog.csdn.net/u014168208/article/details/78129391
-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses

-keep class * implements java.io.Serializable { *; }
-keepattributes *Annotation
-keepattributes Signature
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }

##---------------FastJson Proguard END


##---------------Jackson Proguard 参考来源：https://blog.csdn.net/u012573920/article/details/47260123
-keepattributes Signature,*Annotation*,*EnclosingMethod*
-keep class org.codehaus.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *; }

##---------------Jackson Proguard END