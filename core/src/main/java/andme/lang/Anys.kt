package andme.lang

import andme.core.isDebuggable
import kotlin.system.measureTimeMillis


/**
 * 调试执行代码
 */
inline fun <T> T.runOnDebug(action: () -> Unit): T {
    if (isDebuggable) {
        action()
    }
    return this
}

/**
 * 此方法看起来与if(){}没有差别，但是此方法可以实现链式调用，而if不能
 */
inline fun <T> T.runOnTrue(condition: Boolean, action: T.() -> Unit): T {
    if (condition) {
        action(this)
    }
    return this
}

var isTimeMonitorEnable: Boolean = true
/**
 * 监控方法运行时间
 */
inline fun runTimeMonitor(
        tag: String = Thread.currentThread().stackTrace[1].methodName,
        func: () -> Unit
):Long {
    val time = measureTimeMillis(func)
    if (!isTimeMonitorEnable)
        return time
    if (time > 500) {
        println("[TimeMonitor]: $tag takes ${time / 1000.0} seconds")
    } else {
        println("[TimeMonitor]: $tag takes $time milliseconds")
    }
    return time
}