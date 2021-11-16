## 简介

本库提供`LeanbackFlexboxLayout`组件，继承自`FlexboxLayout`，用于`FlexboxLayout`在TV开发中的使用场景;

## 使用说明

使用`LeanbackFlexboxLayout`代替`FlexboxLayout`即可；


## 功能说明

### bringToFront兼容修复
修复`FlexboxLayout`中`Child View`调用`bringToFront`方法，会导致该`Child View`在布局的最后绘制的问题；
跟`LinearLayout`同样的问题；


### duplicateChildBringToFront
当布局的`ChildView`调用`bringToFront`方法时，是否当前布局也同步调用`bringToFront`方法，用于布局嵌套时，`bringToFront`的传递问题。

比如当前布局包含几个childview，每个childView与当前view宽度一样，那么当childView
调用`bringToFront`方法，只是在当前布局中置于"浮起来"，但是并不意味着在当前布局所在的Parent中浮起来，导致childview还是会被遮住的问题。

即布局层次关系可能是这样子：

ViewGroup

    -ViewGroup1（LeanbackFlexboxLayout）
    
        -ChildView1
        
        -ChildView2
        
        ...
        
    -ViewGroup2
    
    ...
    

ViewGroup2后于ViewGroup1绘制，当ChildView1调用`bringToFront`方法，只是在ViewGroup1中不被其他childView遮住，但是ViewGroup1还是可能被ViewGroup2遮住；

