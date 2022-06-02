package bas.lib.core

import kotlinx.coroutines.*
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.locks.ReentrantLock
import kotlin.RuntimeException

class CoroutineExceptionTest {

    @Test
    fun testAsyncException() = runBlocking {
        try {
            val task1 = async {
                repeat(10) {
                    delay(100)
                    if (it == 5)
                        throw RuntimeException("task1 throw runtime exception.this is cant caught.")
                    println("${Thread.currentThread().name} task2 $it")
                }
            }

            val task2 = async {
                repeat(10) {
                    delay(100)
                    println("${Thread.currentThread().name} task2 $it")
                }
            }
            throw RuntimeException("this can be caught.")
        } catch (e: Throwable) {
            println("catch exception:${e}")
        }

    }

    @Test
    fun handleAsyncException() {
        val lock = CountDownLatch(1)
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            println("CoroutineExceptionHandler handle exception:${throwable.message}")
            lock.countDown()
        }
        val exceptionHandler2 = CoroutineExceptionHandler { coroutineContext, throwable ->
            println("CoroutineExceptionHandler2 handle exception:${throwable.message}")
            lock.countDown()
        }
        val scope2 = CoroutineScope(Job())
        scope2.launch(exceptionHandler) {
            async {
                async {
                    throw Exception("Failed coroutine")
                }
            }
        }

        val scope = CoroutineScope(SupervisorJob() + exceptionHandler)
        scope.launch {
//            supervisorScope {
            val task1 = launch() {
                repeat(10) {
                    delay(100)
                    if (it == 5)
                        throw RuntimeException("task1 throw runtime exception.this is cant caught.")
                    println("${Thread.currentThread().name} task1 $it")
                }
            }

            kotlin.runCatching {

            }
            println("Task1 = ${task1}")
            val task2 = launch(scope.coroutineContext) {
                repeat(10) {
                    delay(100)
                    println("${Thread.currentThread().name} task2 $it")
                }
            }
//            }
            println("Task2 = ${task2::class.java.name}")
            val task3 = launch(scope.coroutineContext) {
                repeat(10) {
                    delay(100)
                    println("${Thread.currentThread().name} task3 $it")
                }
            }
//            task1.await()
//            task2.await()
//            task3.await()
//            lock.countDown()
        }
        lock.await()
        Thread.sleep(3000)
        try {
            throw RuntimeException("this can be caught.")
        } catch (e: Throwable) {
            println("catch exception:${e}")
        }

    }
}