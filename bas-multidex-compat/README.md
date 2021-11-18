## 本库用于解决5.0之前MultiDex加载问题

### 1、简介
**Android 5.0 之前版本的 MultiDex 支持**
>Android 5.0（API 级别 21）之前的平台版本使用 Dalvik 运行时执行应用代码。默认情况下，
>Dalvik 将应用限制为每个 APK 只能使用一个 classes.dex 字节码文件。为了绕过这一限制，需要使用MultiDex 库。

**Android 5.0 及更高版本的 MultiDex 支持**
>Android 5.0（API 级别 21）及更高版本使用名为 ART 的运行时，它本身支持从 APK 文件加载多个 DEX 文件。
>ART 在应用安装时执行预编译，这会扫描查找 classesN.dex 文件，并将它们编译成单个 .oat 文件，以供 Android 设备执行。
>因此，如果您的 minSdkVersion 为 21 或更高版本，系统会默认启用 MultiDex，并且您不需要 MultiDex 库。

然而`MultiDex.install`是一个非常耗时的过程，`APK`分包越多，耗时越严重，甚至产生`ANR`问题。


### 2、解决办法
字节跳动开源了[BoostMultiDex](https://github.com/bytedance/BoostMultiDex)用于解决上述问题，
从其介绍来说是非常不错的方案，甚至能缩短80%的安装时间。然而可惜我在一款盒子上（4.4系统）采用该方案，直接崩溃了，
所以考虑盒子一般经过厂商修改，可能存在许多未知问题，故该方案不得不放弃（[见issue](https://github.com/bytedance/BoostMultiDex/issues/20)），
因此在盒子上运行只能采取比较保守的方式。

#### 2.1 DefaultMultiDexInstaller
该方案基于原始的`MultiDex.install`方法，开启独立进程进行安装，在独立安装期间，用户可以考虑阻塞或非阻塞主进程继续执行的方式（前提是能管理好非阻塞情况下不能访问非主dex文件中的类）

#### 2.2 BoostMultiDexInstaller
基于字节跳动的安装方案

本库默认使用DefaultMultiDexInstaller方案。

### 3、使用

#### 方式1： 继承MultiDexCompatApplication
该方式只需要让程序中的类继承`MultiDexCompatApplication`即可。如需使用字节跳动方案，则重写如下方法
```
  protected fun getMultidexInstaller():Installer?{
        return BoostMultiDexInstaller()//如果返回null则会使用默认方式
    }
```

#### 方式2：在`Application.attachBaseContext`方法中调用`MultiDexCompat.install(context: Context, installer: Installer? = null)`方法


### 4、额外说明

如果默认安装进程界面效果不符合需求，则可以通过实现Installer自定义过程；或者也可以覆盖当前库配置的安装进程界面实现自己的需求；具体情况视需求而定。