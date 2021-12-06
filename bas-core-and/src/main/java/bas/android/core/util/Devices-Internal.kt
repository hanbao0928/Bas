@file:JvmName("Devices")
@file:JvmMultifileClass


package bas.android.core.util

import android.annotation.TargetApi
import android.os.Build
import android.view.View


/**
 * 生成隐藏虚拟导航栏（华为手机底部虚拟导航栏）的标志
 */
@TargetApi(19)
internal fun generateHideNavigationBarFlag(): Int {
    var flag = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //请求隐藏底部导航栏
            .or(View.SYSTEM_UI_FLAG_IMMERSIVE)  //这个flag只有当设置了SYSTEM_UI_FLAG_HIDE_NAVIGATION才起作用。如果没有设置这个flag，任意的View相互动作都退出SYSTEM_UI_FLAG_HIDE_NAVIGATION模式。如果设置就不会退出
            .or(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION) //让View全屏显示，Layout会被拉伸到NavigationBar下面
    if (Build.VERSION.SDK_INT >= 19)
        flag = flag.or(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)//这个flag只有当设置了SYSTEM_UI_FLAG_FULLSCREEN | SYSTEM_UI_FLAG_HIDE_NAVIGATION 时才起作用。如果没有设置这个flag，任意的View相互动作都坏退出SYSTEM_UI_FLAG_FULLSCREEN | SYSTEM_UI_FLAG_HIDE_NAVIGATION模式。如果设置就不受影响
    return flag
}