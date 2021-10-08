package com.bas.android.leanback

import com.bas.core.android.basCtx
import com.bas.core.android.util.isTVUIMode

/**
 * Created by Lucio on 2021/9/30.
 */
object Leanback {

    val isLeanbackMode:Boolean  = basCtx.isTVUIMode()

}