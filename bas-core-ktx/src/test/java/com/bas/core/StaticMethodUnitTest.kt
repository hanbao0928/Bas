package com.bas.core

import bas.lang.NOW
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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

    @Test
    fun testProgress() = runBlocking {
        println("当前线程@${Thread.currentThread().name}")
        var progress = 0
        do{
            withContext(Dispatchers.Default) {
                delay(500)
                progress += Random.nextInt(1, 10)
            }
            println("当前进度：${progress.coerceAtMost(100)}")
        }while(progress < 100)
    }
}