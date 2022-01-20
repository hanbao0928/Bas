package com.bas.android.leanback.event

import android.view.KeyEvent
import bas.android.core.util.Logger

/**
 * Created by Lucio on 2021/9/27.
 * 按键策略,即设定指定的按键顺序，如果遥控器的按键按照设定的顺序执行，则触发[action]的调用
 *
 * sample:比如设置各按五次上下键之后执行相关逻辑
 * 1、先创建策略
 *   val keys = mutableListOf<Key>()
 *   repeat(5){
 *      keys.add(Key(KeyEvent.KEYCODE_DPAD_UP,KeyEvent.ACTION_DOWN))
 *      keys.add(Key(KeyEvent.KEYCODE_DPAD_DOWN,KeyEvent.ACTION_DOWN))
 *   }
 *   val strategy = KeyEventStrategy.create(keys){
 *      println("执行按键策略方法体")
 *   }
 * 2、在[android.app.Activity.dispatchKeyEvent]调用策略的[KeyEventStrategy.onKeyEventStrategy]方法即可
 *  override fun dispatchKeyEvent(event: KeyEvent): Boolean {
 *      debugKeyEventStrategy.onKeyEventStrategy(event)
 *      return super.dispatchKeyEvent(event)
 *  }
 *
 * @param keys 按键集合
 * @param action 策略执行的方法
 */
class KeyEventStrategy private constructor(
    private val firstKey: KeyFactor,
    val action: () -> Unit
) {

    private class KeyFactor(val keyCode: Int, val action: Int,val keyIndex:Int) {
        var next: KeyFactor? = null
    }

    private var current: KeyFactor? = null

    /**
     * 执行key event 策略
     * @return true：策略通过，执行了策略方法
     */
    fun onKeyEventStrategy(event: KeyEvent): Boolean {
        var key = current
        if (key == null) {
            key = firstKey
        }

        if (key.action != event.action) {
            Logger.d(
                TAG,
                "action not equal,return. key.action=${key.action} event.action=${event.action}"
            )
            return false
        }

        if (key.keyCode == event.keyCode) {
            if (key.next == null) {
                dispatchAction()
                return true
            } else {
                current = key.next
                Logger.d(
                    TAG,
                    "action and keycode equal,return. current key(index = ${key.keyIndex}) consumed. wait next key event."
                )
            }
        } else {
            Logger.w(TAG, "keycode not equal,interrupt strategy.key.keyCode=${key.keyCode} event.keyCode=${event.keyCode}")
            current = null
        }
        return false
    }

    private fun dispatchAction() {
        Logger.i(TAG, "invoke strategy key event action.")
        action()
        current = null
    }


    class Key(val keyCode: Int, val focusAction: Int)

    companion object {

        private const val TAG = "KeyEventStrategy"

        @JvmStatic
        fun create(keys: List<Key>, action: () -> Unit): KeyEventStrategy {
            if (keys.isNullOrEmpty())
                throw IllegalArgumentException("keys must be not empty.")
            var next: KeyFactor? = null

            val size = keys.size
            keys.reversed().forEachIndexed { index, key ->
                val factor = KeyFactor(key.keyCode, key.focusAction,size - index - 1)
                factor.next = next
                next = factor
            }

            return KeyEventStrategy(next!!, action)
        }

        @JvmStatic
        fun default(action: () -> Unit): KeyEventStrategy {
            val keys = mutableListOf<Key>()
            repeat(3) {
                keys.add(Key(KeyEvent.KEYCODE_DPAD_UP, KeyEvent.ACTION_UP))
                keys.add(Key(KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.ACTION_UP))
            }

            repeat(2) {
                keys.add(Key(KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.ACTION_UP))
                keys.add(Key(KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.ACTION_UP))
             
            }
            return create(keys, action)
        }
    }
}