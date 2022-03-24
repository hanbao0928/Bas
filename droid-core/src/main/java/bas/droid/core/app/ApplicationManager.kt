/**
 * Created by Lucio on 2020-02-13.
 * app管理接口
 */
package bas.droid.core.app

import bas.droid.core.app.internal.ApplicationManagerImpl

@JvmField
val AppManager: ApplicationManager = ApplicationManagerImpl

interface ApplicationManager {

    /**
     * 当前app是否前台可见
     */
    val isForeground: Boolean

    /**
     * activity堆栈
     */
    val activityStack: ActivityStack

    /**
     * 绑定状态变化监听
     */
    fun registerAppStateChangedListener(listener: OnAppStateChangedListener)

    /**
     * 去除状态变化监听
     */
    fun unregisterAppStateChangedListener(listener: OnAppStateChangedListener)

    /**
     * 是否支持调试：控制一些调试逻辑、调试日志的控制等
     */
    var isDebuggable: Boolean

    interface OnAppStateChangedListener {

        /**
         * 前台运行（当前用户操作焦点）
         */
        fun onAppBecameForeground()

        /**
         * 后台运行
         */
        fun onAppBecameBackground()
    }
}