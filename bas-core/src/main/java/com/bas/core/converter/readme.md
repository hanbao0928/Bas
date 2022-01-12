
# Converters

Converters解决序列化和反序列化问题；


## 使用说明
Converters默认提供了如下第三方库的支持：
- (Jackson)[https://github.com/FasterXML/jackson-dataformat-xml]
需要在工程中添加Jackson 、Gson或FastJson；如果工程依赖


## TODO：完善各个库对Kotlin的支持

https://www.jianshu.com/p/4e206ee7dca0?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes&utm_source=recommendation

>Moshi是Square公司在2015年6月开源的有关Json的反序列化及序列化的框架，
说到Json，大家应该很快想到Gson，FastJson以及Jackson等著名的开源框架，
那为什么还需要Moshi呢？这个主要是由于Kotlin的缘故，我们知道前面说到的几大解析库主要是针对Java解析Json的，
当然他们也支持Kotlin，但是Moshi天生对Kotlin友好，而且对Java的解析也毫不逊色，
所以不管是在Java跟Kotlin的混编还是在纯Kotlin项目中，Moshi表现都很出色。
在Java当中，Gson是官方推荐的反序列化及序列化Json框架，同样在Kotlin中，
也有官方的库kotlinx.serialization，下面简称KS，这个库在kotlinx中，是单独拿出来了
，跟kotlinx.coroutines一样，接下来我们拿官方库Gson以及Kotlin的官方库KS来做个对比，看看彼此的特点。
>

Moshi比较适合Kotlin和Java混编(但是moshi的tojson不是很好用，需要额外参数，需改进现有框架)

kotlinx.serialization适合kotlin&跨平台（kotlin.native）

目前使用的jackson-module-kotlin解决当前项目问题。


kotlinx.serialization：
 https://zhuanlan.zhihu.com/p/444782955
 https://blog.csdn.net/vitaviva/article/details/104779479
 
jackson-module-kotlin:
 https://github.com/FasterXML/jackson-module-kotlin