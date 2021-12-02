package com.bas

import com.bas.core.lang.NOW
import org.junit.Test
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread
import kotlin.random.Random

/**
 * Created by Lucio on 2021/11/13.
 */
class StaticMethodUnitTest {

    companion object {

        @Synchronized
        @JvmStatic
        fun wastTimeSync() {
            Thread.sleep(1000)
        }

        @JvmStatic
        fun wastTime() {
            Thread.sleep(1000)
        }
    }

    private fun log(msg: String) {
        println("${NOW.time} $msg")
    }

    private inline fun monitorTime(body: () -> Unit) {
        val start = NOW.time
        println("${NOW.time} ${Thread.currentThread().name}:工作开始")
        body.invoke()
        println("${NOW.time} ${Thread.currentThread().name}:工作结束，耗时${NOW.time - start}")
    }

    @Test
    fun testMultiThreadInvokeSyncMethod() {
        monitorTime {
            val threadCount = 5
            val lock = CountDownLatch(threadCount)
            repeat(threadCount) {
                thread {
                    monitorTime {
                        wastTimeSync()
                    }
                    lock.countDown()
                }
            }
            lock.await()
        }
    }

    @Test
    fun testMultiThreadInvokeMethod() {
        monitorTime {
            val threadCount = 5
            val lock = CountDownLatch(threadCount)
            repeat(threadCount) {
                thread {
                    monitorTime {
                        wastTime()
                    }
                    lock.countDown()
                }
            }
            lock.await()
        }
    }
}