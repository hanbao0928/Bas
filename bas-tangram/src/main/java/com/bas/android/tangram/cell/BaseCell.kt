package com.bas.android.tangram.cell

import com.bas.android.tangram.Tangrams

/**
 * Created by Lucio on 2021/10/1.
 */
class BaseCell {
    var mActivated = false

    /*
     * Manage card lifecycle
     */
    fun added() {
        check(!(mActivated && Tangrams.isPrintLog())) { "Component can not be added more than once" }
        mActivated = true
        onAdded()
    }

    fun removed() {
        check(!(!mActivated && Tangrams.isPrintLog())) { "Component can not be removed more than once" }
        mActivated = false
        onRemoved()
    }


    protected open fun onAdded() {}

    protected open fun onRemoved() {}
}