

## 1.简介
本库用于解决图片加载场景，所提供的功能均在接口`ImageLoaderEngine`中定义，并通过`ImageLoader`提供对外访问


## 2.使用

调用`ImageLoader#init(Context)`方法进行初始化
>之前考虑使用`androidx.startup.Initializer`进行自动初始化，但采用此方式弊大于利（可能增加启动延迟，也考虑调用方延迟初始化的可能：比如子进程不使用该组件）


此外您无需做其他操作即可使用`ImageLoader`或者基于`ImageLoader`提供的扩展方法（`ImageLoaderExtensions`文件中定义的方法）进行图片加载;
但为了您更好的使用，可以做后续了解。

### 2.1 设置默认配置
通过`ImageLoader#setConfig`方法增加全局配置；具体支持的配置信息参见`ImageLoader.Config`定义
- 可以设置缓存目录名字
- 是否启用 disk cache
- 是否启用 memory ache
- 默认图片资源
- 默认圆角图片资源
- 默认圆形图片资源

## 2.2 设置Engine
通过`ImageLoader#setEngine`方法可以切换具体的加载实现；

本库提供`GlideEngine`(基于`Glide`)的`ImageLoaderEngine`实现，并作为默认配置功能；
如果您也计划使用`Glide`作为图片加载的实现，并且通过`ImageLoader#setConfig`修改了默认配置信息，或者您计划使用Glide的Generate Api，
则您应该在您的app module中增加如下配置：
```
@GlideModule
public class MyAppGlideModule extends GlideEngineModule {

}

```
如果您的工程中已经包含`@GlideModule`的定义，则您可以考虑将父类修改为`GlideEngineModule`，
或者将`GlideEngineModule`内的实现复制到您的`@GlideModule`类中，
`GlideEngineModule`内实现的逻辑只是为了确保`ImageLoader#setConfig`设置的配置项能够生效。


## 2.3 资源覆盖
以下资源可以通过覆盖达到修改：

```

<!--图片圆角半径-->
<dimen name="image_rounded_radius_bas">6dp</dimen>

<!--占位图颜色-->
<color name="image_placeholder_color_bas">#26FFFFFF</color>

<!--默认占位图文件-->
../drawable/image_placeholder_bas.xml

<!--默认圆角占位图文件-->
../drawable/rounded_image_placeholder_bas.xml

<!--默认圆形占位图文件-->
../drawable/circle_image_placeholder_bas.xml
```
