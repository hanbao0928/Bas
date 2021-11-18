#保持库中所有代码
-keep class com.bytedance.boost_multidex.** { *; }

#保持本库定义类
-keep public class com.bas.android.multidex.** {
    public *;
    protected *;
}