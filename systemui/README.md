## 本库说明
本库用于手机状态栏和虚拟导航栏的设置；支持透明状态栏、设置状态栏颜色等；

## 引入说明
在工程（Project）的build.gradle文件中配置仓库地址
```
allprojects {
    repositories {
        ...
        /*配置仓库地址*/
        maven {
            url "http://162.14.111.89:8081/repository/droid-app-release/"
        }
    }
}

```

在对应模块（Module）的build.gradle中添加依赖
```
implementation("bas.droid:systemui:0.0.1")
```

配置完成。

## 功能说明
使用方式：
- 提供的所有功能可以通过`systemUiHandler`进行调用；
- 使用kotlin编码的工程也可以通过为`Activity`扩展的对应方法直接调用；
- 提供了一个名为`StatusBarFakerView`并且与状态栏等高的自定义View，用于需要在xml布局中替代状态栏区域的场景；

虚拟导航栏的控制与状态栏的控制很相似，以下以状态栏进行说明，具体细节查看源文件`SystemUi.kt`

#### 1、修改状态栏颜色
通过`systemUiHandler#setStatusBarColor`或在`Activity#applyStatusBarColor`方法修改状态栏颜色（用户布局内容不会显示在状态栏之下）；

#### 2、沉浸式状态栏
- 支持沉浸式模式（即用户布局显示在状态栏之下）
- 支持修改状态栏颜色（支持纯色、混色）
通过`systemUiHandler#setImmersiveStatusBar`或使用`Activity#applyImmersiveStatusBar`方法实现。

#### 3、修改虚拟导航栏颜色&设置沉浸式导航栏
导航栏操作类似状态栏操作，不赘述

#### 4、同时修改状态栏和导航栏颜色或设置沉浸式模式
参考`systemUiHandler#setSystemBarColor`或`Activity#applySystemBarColor`方法修改颜色；

参考`systemUiHandler#setImmersiveSystemBar`或`Activity#applyImmersiveSystemBar`方法设置沉浸式模式或同时设置颜色；

#### 5、状态栏浅色模式或深色模式
- 浅色模式（即状态栏背景为浅色、文字为深色（比如黑色））：参考`systemUiHandler#setStatusBarLightMode`或`Activity#applyStatusBarLightMode`方法；
- 深色模式（即状态栏背景为深色、文字为浅色（比如白色））：参考`systemUiHandler#setStatusBarDarkMode`或`Activity#applyStatusBarDarkMode`方法；
