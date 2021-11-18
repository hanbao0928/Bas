package com.bas.android.multidex

import android.content.Context
import com.bytedance.boost_multidex.BoostMultiDex

/**
 * Created by Lucio on 2021/11/17.
 * 使用字节跳动的安装方式：在机顶盒上会闪退，暂时不能启用该方式
 */
class BoostMultiDexInstaller : Installer {

    override fun doInstall(context: Context) {
        val result: com.bytedance.boost_multidex.Result? = BoostMultiDex.install(context)
        if (result?.fatalThrowable != null) {
            loge("exception occored on BoostMultiDexInstaller.doInstall ",result.fatalThrowable)
        }
    }

    /**
     * 不用阻塞，[BoostMultiDex.install]内部已经阻塞了进程，是一个同步安装过程
     */
    override val isSyncMode: Boolean = false

    /**
     * 第二次安装
     */
    override fun doSecondInstall(context: Context) {
        //nothing
    }
}