package com.bas.android.multidex

import android.content.Context

/**
 * Created by Lucio on 2021/11/17.
 */
interface Installer {

    fun doInstall(context: Context)

    /**
     * 安装过程是否是同步过程：[androidx.multidex.MultiDex.install]就是一个同步安装过程
     */
    val isSyncMode: Boolean


    /**
     * 第二次安装
     */
    fun doSecondInstall(context: Context)
}