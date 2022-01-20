package andme.core.content

import andme.core.mApp
import andme.core.sysui.FakeStatusBar

/**
 * Created by Lucio on 2020-11-09.
 */

/**
 * 状态栏高度
 */
val statusBarHeight: Int by lazy {
    FakeStatusBar.getAvailableStatusBarHeight(mApp)
}

