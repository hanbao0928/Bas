package com.bas.core.android.util

import android.util.Log
import androidx.annotation.IntDef

/**
 * Created by Lucio on 2021/9/17.
 */
object Logger {

    /**
     * Priority constant for the println method; use Log.v.
     */
    const val VERBOSE = 2

    /**
     * Priority constant for the println method; use Log.d.
     */
    const val DEBUG = 3

    /**
     * Priority constant for the println method; use Log.i.
     */
    const val INFO = 4

    /**
     * Priority constant for the println method; use Log.w.
     */
    const val WARN = 5

    /**
     * Priority constant for the println method; use Log.e.
     */
    const val ERROR = 6

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(VERBOSE, DEBUG, INFO, WARN, ERROR)
    annotation class Level

    @JvmStatic
    @Level
    private var level: Int = VERBOSE

    /**
     * 设置日志等级
     */
    @JvmStatic
    fun setLevel(@Level level: Int) {
        Logger.level = level
    }

    @JvmStatic
    fun v(tag: String, msg: String) {
        if (level > VERBOSE)
            return
        Log.d(tag, msg)
    }

    @JvmStatic
    fun v(tag: String, msg: String, tr: Throwable) {
        if (level > VERBOSE)
            return
        Log.d(tag, msg, tr)
    }

    @JvmStatic
    fun d(tag: String, msg: String) {
        if (level > DEBUG)
            return
        Log.d(tag, msg)
    }

    @JvmStatic
    fun d(tag: String, msg: String, tr: Throwable) {
        if (level > DEBUG)
            return
        Log.d(tag, msg, tr)
    }

    @JvmStatic
    fun i(tag: String, msg: String) {
        if (level > INFO)
            return
        Log.i(tag, msg)
    }

    @JvmStatic
    fun i(tag: String, msg: String, tr: Throwable) {
        if (level > INFO)
            return
        Log.i(tag, msg, tr)
    }

    @JvmStatic
    fun w(tag: String, msg: String) {
        if (level > WARN)
            return
        Log.w(tag, msg)
    }

    @JvmStatic
    fun w(tag: String, msg: String, tr: Throwable) {
        if (level > WARN)
            return
        Log.w(tag, msg, tr)
    }

    @JvmStatic
    fun e(tag: String, msg: String) {
        if (level > ERROR)
            return
        Log.e(tag, msg)
    }

    @JvmStatic
    fun e(tag: String, msg: String, tr: Throwable) {
        if (level > ERROR)
            return
        Log.e(tag, msg, tr)
    }

}