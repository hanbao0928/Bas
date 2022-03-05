## API扩展-[Generated API](https://muyangmin.github.io/glide-docs-cn/doc/generatedapi.html)
如果想跟RequestOptions扩展方法，或者获取加载的资源类型，或者统一生成指定api，则可以参考[Generated API](https://muyangmin.github.io/glide-docs-cn/doc/generatedapi.html)

## 避免在程序库中使用 AppGlideModule
https://muyangmin.github.io/glide-docs-cn/doc/configuration.html#%E9%81%BF%E5%85%8D%E5%9C%A8%E7%A8%8B%E5%BA%8F%E5%BA%93%E4%B8%AD%E4%BD%BF%E7%94%A8-appglidemodule

## 占位图
[链接末尾有说明](https://muyangmin.github.io/glide-docs-cn/doc/placeholders.html)
### 占位符是异步加载的吗？
No。占位符是在主线程从Android Resources加载的。我们通常希望占位符比较小且容易被系统资源缓存机制缓存起来。

### 变换是否会被应用到占位符上？
No。Transformation仅被应用于被请求的资源，而不会对任何占位符使用。
在应用中包含必须在运行时做变换才能使用的图片资源是很不划算的。相反，在应用中包含一个确切符合尺寸和形状要求的资源版本几乎总是一个更好的办法。假如你正在加载圆形图片，你可能希望在你的应用中包含圆形的占位符。另外你也可以考虑自定义一个View来剪裁(clip)你的占位符，而达到你想要的变换效果。

### 在多个不同的View上使用相同的Drawable可行么？
通常可以，但不是绝对的。任何无状态(non-stateful)的 Drawable（例如 BitmapDrawable ）通常都是ok的。但是有状态的 Drawable 不一样，在同一时间多个 View 上展示它们通常不是很安全，因为多个View会立刻修改(mutate) Drawable 。对于有状态的 Drawable ，建议传入一个资源ID，或者使用 newDrawable() 来给每个请求传入一个新的拷贝。


## 选项-(RequestOptions)[https://muyangmin.github.io/glide-docs-cn/doc/options.html]

### RequestOptions对象可重用
如果你想让你的应用的不同部分之间共享相同的加载选项，你也可以初始化一个新的 RequestOptions 对象，并在每次加载时通过 apply() 方法传入这个对象：
### RequestOptions可以组合使用
apply() 方法可以被调用多次，因此 RequestOption 可以被组合使用。如果 RequestOptions 对象之间存在相互冲突的设置，那么只有最后一个被应用的 RequestOptions 会生效。

### RequestBuilder 也可以被复用于开始多个请求
```
RequestBuilder<Drawable> requestBuilder =
        Glide.with(fragment)
            .asDrawable()
            .apply(requestOptions);

for (int i = 0; i < numViews; i++) {
   ImageView view = viewGroup.getChildAt(i);
   String url = urls.get(i);
   requestBuilder.load(url).into(view);
}
```