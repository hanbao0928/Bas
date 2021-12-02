## 说明
本库用于生成`Leanback`布局系列

## 使用说明

## 1、引入依赖
```
    annotationProcessor project(':lib_complier')
    implementation project(':lib_annotations')

    implementation project(path: ':bas-leanback-layout-annotation')
    //如果是kotlin，则根据情况需要修改为kapt
    annotationProcessor project(path: ':bas-leanback-layout-compiler')
```

## 2、生成所需布局
任意创建一个类，并通过`LeanbackLayout`注解引入您所需要生成的布局对象
```java
//任意创建一个对象,然后通过注解添加引入所需使用的布局
@LeanbackLayout({LinearLayout.class, RelativeLayout.class})
public class LeanbackLayoutInject {
}

```
创建以上代码之后，编译当前工程，则会自动生成`LeanbackLinearLayout`、`LeanbackRelativeLayout`；

生成说明：
- 生成类名规则：在原有名字的基础上，增加`Leanback`前缀，比如`LinearLayout`，生成之后为`LeanbackLinearLayout`
- 支持任意`ViewGroup`生成（包括自定义布局）
- `bas-leanback-core`库已经生成了常规布局：`LinearLayout`、`FrameLayout`、`RelativeLayout`、`ConstraintLayout`等
- `bas-leanback-flexbox`库已经提供了`LeanbackFlexboxLayout`

