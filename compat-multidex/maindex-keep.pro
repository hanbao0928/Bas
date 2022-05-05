
# 声明主要 DEX 文件中必需的类 https://developer.android.google.cn/studio/build/multidex?hl=zh_cn#keep

# 包含本库所有代码
-keep class bas.droid.compat.multidex.**

# 如果使用官方方案，则应包含MultiDex库的所有代码
-keep class androidx.multidex.**

# 如果使用字节方案，则应包含字节库的所有代码
-keep class com.bytedance.boost_multidex.**

#-keep class kotlin.Metadata
#-keep class kotlin.Unit
#-keep class kotlin.text.StringsKt
#-keep class kotlin.jvm.JvmStatic
#-keep class kotlin.jvm.JvmOverloads
#-keep class kotlin.jvm.functions.Function0
#-keep class kotlin.jvm.internal.Intrinsics
#-keep class kotlin.jvm.internal.Lambda
#-keep class  kotlin.jvm.internal.DefaultConstructorMarker
#
#-keep class org.jetbrains.annotations.NotNull
#-keep class  org.jetbrains.annotations.Nullable





