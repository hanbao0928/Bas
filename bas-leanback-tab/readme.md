# 1.简介
更多介绍请查看文章：[传送门](https://juejin.cn/editor/drafts/7018848252329984037)

本库适用于解决TabLayout和ViewPager在TV开发中的场景（同时支持手机中运行）

本库主要提供`LeanbackTabLayout`和`LeanbackViewPager`两个组件。

`LeanbackTabLayout`提供了如下功能
- TV操作支持设置：可以设置通过focus改变响应tab选中事件
- 焦点记忆
- 边界寻焦点规则优化：设置边界焦点移出与否
- 与ViewPager更好的联用
- 自定义Tab样式

`LeanbackViewPager`提供如下功能：
- 设置启用/禁用`TouchEvent`
- 设置启用/禁用`KeyEvent`
- 边界寻焦点规则优化：设置边界焦点移出与否

# 2.使用说明
该库使用非常简单，查看LeanbackTabLayout的`setupWithViewPager`方法即可明白；
其他功能后文列出了对应的api和属性，随时调用设置即可。

```
//最简单的使用方式：Tab会根据Adapter的getPageTitle方法显示文本Tab效果
tabLayout.setupWithViewPager(tabViewPager)

//自定义Tab配置策略使用方式：库内置了几种Strategy，ViewPagerStrategy对应系统的处理方式，也就是跟前面的使用方式是一样的，其他策略见TabConfigurationStrategy
tabLayout.setupWithViewPager(
    tabViewPager,TabConfigurationStrategy.ViewPagerStrategy(binding.tabViewPager)
)

```
# 3.API说明
## 3.1 `LeanbackViewPager`
### 3.1.1 设置启用/禁用`TouchEvent`（默认禁用）
对应xml属性：`app:touchEnabled_lbt`
```kotlin
fun setTouchEnabled(enableTouch: Boolean)
```
### 3.1.2 设置启用/禁用`KeyEvent`（默认禁用）
对应xml属性：`app:keyEventEnabled_lbt`
```kotlin
fun setKeyEventsEnabled(enableKeyEvent: Boolean)
```
### 3.1.3 设置边界焦点移出与否（默认不允许）
对应xml属性：`app:focusOutEnabled_lbt`
```
/**
 * 优化边界寻焦规则：触发遥控器左右按键时，是否允许[ViewPager]内的焦点转移到[ViewPager]之外
 */
fun setFocusOutEnabled(enableFocusOut: Boolean) {
    focusOutEnabled = enableFocusOut
}
```

## 3.2 `LeanbackTabLayout`
`LeanbackTabLayout`是基于`TabLayout`进行扩展的，所以`TabLayout`本身支持的功能基本上都是支持的，比如设置`fixed tab`或者`scroll tab`、设置`tab`选中的`indicator`（`tab`下面的横线）等。
### 3.2.1 `TV`操作支持
对应xml属性：`app:isLeanbackMode_lbt`
在`TV`上运行时，通过`Tab`的`Focus`触发`Tab`的选中事件（手机上是通过`Tab`的点击）
```
fun setLeanbackMode(isLeanback: Boolean)
```
默认支持`TV`操作，因为即便开启`TV`操作支持，也不影响在手机上运行。

*注：本库的焦点事件绑定较为简单明了。*

### 3.2.2 边界寻焦规则优化
对应xml属性：`app:focusOutEnabled_lbt`
```
/**
 * 优化边界寻焦规则：触发遥控器左右按键时，是否允许[TabLayout]内的焦点转移到[TabLayout]之外
 */
fun setFocusOutEnabled(enableFocusOut: Boolean) {
    focusOutEnabled = enableFocusOut
}
```

### 3.2.3 焦点记忆
对应xml属性：`app:focusMemoryEnabled_lbt`
`TabLayout`获取焦点时，之前已选中的`tab`优先获取焦点
```
fun setFocusMemoryEnabled(enableFocusMemory: Boolean)
```

### 3.2.4 `ViewPager`联动使用场景、自定义`Tab`处理等
支持系统的`setupWithViewPager`方法
```kotlin
fun setupWithViewPager(viewPager: ViewPager?, autoRefresh: Boolean)
```

支持与`ViewPager`联用时自定义`Tab`样式，可以轻易做到文本、文本图标、自定义、甚至完全混合样式的Tab使用场景。

```kotlin
/**
 * 根据ViewPager进行初始化；
 * @param tabConfigurationStrategy tab配置策略，用于自定义Tab样式；
 * [TabConfigurationStrategy.TextStrategy]:文本tab策略
 * [TabConfigurationStrategy.ViewPagerStrategy]:（系统TabLayout与ViewPager联用时的规则相似） 使用ViewPager的adapter提供的getPageTitle方法创建文本Tab策略
 * [TabConfigurationStrategy.TextIconStrategy]:文本+图标策略
 * [TabConfigurationStrategy.CustomViewStrategy]：自定义view策略,[TabConfigurationStrategy.CustomViewFactory]
 * 如果以上策略不能满足需求，可以自定义实现[TabConfigurationStrategy]。
 * @param autoRefresh [ViewPager.getAdapter]数据改变时是否自动刷新Tab
 */
fun setupWithViewPager(
    viewPager: ViewPager?,
    tabConfigurationStrategy: TabConfigurationStrategy,
    autoRefresh: Boolean = true
) 
```

### 3.2.5 Adapter与Tab的刷新
只要`ViewPager`更改了`Adapter`，或者调用了`Adapter`的`notifyDataSetChanged
`方法通知刷新更新，`TabLayout`将同步更新。


### 3.2.6 其他说明
`TabLayout`与`ViewPager`的联动实现已完全封装到`LeanbackTabLayoutMediator`类中，该类也可以单独使用。

## 4.粗略Demo截图

![text_tab.jpg](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ac2bc736837c4005919aa00f7391f66f~tplv-k3u1fbpfcp-watermark.image?)

![text_icon_tab.jpg](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/41e3c5d602fb4414920e4ee615375475~tplv-k3u1fbpfcp-watermark.image?)

![custom_tab.jpg](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/9817bceb68014533be349bbb2971f7f6~tplv-k3u1fbpfcp-watermark.image?)

![mixed_tab.jpg](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/fe844fc14acb41da94bf0830bae320af~tplv-k3u1fbpfcp-watermark.image?)


![refresh.jpg](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/930579a65e4e4f1782836e66653f7654~tplv-k3u1fbpfcp-watermark.image?)

