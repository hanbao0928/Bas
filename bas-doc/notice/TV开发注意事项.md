# LinearLayout的Child View获取焦点时顺序变化的问题

```
//假定有如下布局
<LinearLayout>
    <Button android:id="@+id/id1"/>
    <Button android:id="@+id/id2"/>
    <Button android:id="@+id/id3"/>
    <Button android:id="@+id/id4"/>
</LinearLayout>
```

当id1按钮获取焦点之后，调用`bringToFront()`方法，会导致id1按钮排列到LinearLayout的末端。

原因：LinearLayout不适用于z轴,因此,它的名称是线性的。也就是LinearLayout的子View不适合调用`bringToFront()`方法。

解决办法：改为RelativeLayout、ConstraintLayout、FrameLayout等。或者去掉`bringToFront()`方法的调用，在LinearLayout中不会存在覆盖的问题。