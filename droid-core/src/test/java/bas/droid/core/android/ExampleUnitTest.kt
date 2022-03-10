package bas.droid.core.android

import bas.lib.core.coroutines.asyncRetryable
import bas.lib.core.coroutines.launchRetryable
import bas.lib.core.date.now
import bas.lib.core.lang.exception.RetryException
import bas.lib.core.date.dateTimeFormatCN
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun retryableAsyncTest() = runBlocking{
        var flag = 3
        val result = asyncRetryable {
            if(flag >0){
                println("发生重试错误,即将重试")
                delay(1000)
                flag--
                println("${now().dateTimeFormatCN}开始重试")
                throw RetryException()
            }else{
                println("获取到正常结果")
                null
            }
        }.await()
        println("retryableAsync结果=$result")
    }

    @Test
    fun retryableLaunchTest() = runBlocking{
        var flag = 3
        val result = launchRetryable {
            if(flag >0){
                println("发生重试错误,即将重试")
                delay(1000)
                flag--
                println("${now().dateTimeFormatCN}开始重试")
                throw RetryException()
            }else{
                println("获取到正常结果")
                flag
            }
        }
        println("retryableLaunch结果=$result")
    }
}