package com.bas.core

import bas.lang.FormatDateUseCase
import bas.lang.WeekDateUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Lucio on 2022/1/19.
 */
class WeekUnitTest {

    @Test
    fun testGetWeek() = runBlocking {
        val ft = FormatDateUseCase("yyyy-MM-dd")
        val usecase1 = WeekDateUseCase(Calendar.MONDAY)
        val dates1 = usecase1.invoke()
        println("获取当前日期所在的周一到周日")
        dates1.forEach {
           println(ft(it))
        }


        val usecase2 = WeekDateUseCase(Calendar.SUNDAY)
        val dates2 = usecase2.invoke()
        println("获取当前日期所在的周日、周一到周六")
        dates2.forEach {
            println(ft(it))
        }


        val usecase3 = WeekDateUseCase(Calendar.THURSDAY)
        val dates3 = usecase3.invoke()
        println("获取当前日期所在的周四、周五、周六、周日、周一、周二、周三")
        dates3.forEach {
            println(ft(it))
        }

        val usecase4 = WeekDateUseCase(Calendar.FRIDAY)
        val dates4 = usecase4.invoke()
        println("获取当前日期所在的周五、周六、周日、周一、周二、周三、周四")
        dates4.forEach {
            println(ft(it))
        }
    }

    fun printWeek(date: Date, locale: Locale) {
        val df = SimpleDateFormat("yyyy-MM-dd") //设置日期格式

        val cld = Calendar.getInstance(locale)
                cld.setFirstDayOfWeek(Calendar.MONDAY);//以周一为首日
        cld.timeInMillis = date.time //当前时间
//
//        cld.setFirstDayOfWeek(Calendar.SUNDAY);//以周一为首日


        cld[Calendar.DAY_OF_WEEK] = Calendar.MONDAY //周一

        println(df.format(cld.time))

        cld[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY //周日

        println(df.format(cld.time))
    }
}