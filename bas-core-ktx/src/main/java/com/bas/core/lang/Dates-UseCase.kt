/**
 * Created by Lucio on 2022/1/7.
 * 日期相关用例
 */
package com.bas.core.lang

import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * 日期格式化用例
 */
class FormatDateUseCase(pattern: String, locale: Locale = Locale.getDefault()) {

    private val formatter: SimpleDateFormat = SimpleDateFormat(pattern, locale)

    operator fun invoke(date: Date): String {
        return formatter.format(date)
    }

}

/**
 * 一周日期获取用例
 * @param firstDayOfWeek 指定周的第一天，比如指定为周一或周天，甚至指定为任意星期几
 * @return 返回指定日期所在周的日期（7天日期）
 *
 * 如[firstDayOfWeek]指定为周一，则返回的结果为当前日期所在周的周一、一直到周日
 * 如[firstDayOfWeek]指定为周日，则返回的结果为当前日期所在周的周日、周一、一直到周六
 * 如[firstDayOfWeek]指定为周三，则返回的结果为当前日期所在周的周三、周四、一直到下周二
 */
class WeekDateUseCase(
    private val firstDayOfWeek: Int = Calendar.MONDAY,
    private val locale: Locale = Locale.getDefault()
) {
    operator fun invoke(date: Date = Date()): Array<Date> {
        val cld = Calendar.getInstance(locale)
        //设置一周的首天：比如通常情况为周一
        cld.firstDayOfWeek = firstDayOfWeek
        //设置当前时间
        cld.time = date

        val weekDayNum = 7

        return (0 until weekDayNum).map { offset ->
            val index = (firstDayOfWeek + offset) % weekDayNum
            cld[Calendar.DAY_OF_WEEK] = index
            cld.time
        }.toTypedArray()
    }
}

/**
 * 获取范围日期用例
 */
class RangeDateUseCase(
    timeZone: TimeZone = TimeZone.getDefault(),
    locale: Locale = Locale.getDefault()
) {

    private val calendar = Calendar.getInstance(timeZone, locale)

    /**
     * 范围日期用例：获取指定日期[targetDate]前[beforeDateCount]个日期和指定日期[targetDate]后[afterDateCount]个日期组成的日期集合
     * @param targetDate 目标日期
     * @param beforeDateCount 目标日期之前取多少个日期
     * @param afterDateCount 目标日期之后取多少个日期
     * @return [beforeDateCount] + [targetDate] + [afterDateCount] 拼接成的日期列表
     */
    operator fun invoke(
        targetDate: Date,
        beforeDateCount: Int,
        afterDateCount: Int,
    ): List<Date> {
        val items = mutableListOf<Date>()
        calendar.time = targetDate
        calendar.add(Calendar.DAY_OF_MONTH, -beforeDateCount)
        repeat(beforeDateCount - 1) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            items.add(calendar.time)
        }

        items.add(targetDate)

        calendar.time = targetDate
        repeat(afterDateCount) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            items.add(calendar.time)
        }
        return items
    }

}

/**
 * 以周为单位获取日期范围
 */
class WeeklyRangeDateUseCase(
    firstDayOfWeek: Int = Calendar.MONDAY,
    timeZone: TimeZone = TimeZone.getDefault(),
    locale: Locale = Locale.getDefault()
) {

    private val weeklyDateUseCase = WeekDateUseCase(firstDayOfWeek, locale)
    private val rangeDateUseCase = RangeDateUseCase(timeZone, locale)

    /**
     * 以周为单位获取范围日期：获取指定日期[targetDate]前[beforeWeekCount]个周日期和指定日期[targetDate]后[afterWeekCount]个周日期组成的日期集合
     * ：比如指定日期为 12-01，  [beforeWeekCount] = 2,[afterWeekCount] = 1,则获取的日期为12-01当周的日期加上这个周之前的两个周日期和之后的一个周日期
     * @param targetDate 指定日期
     * @param beforeWeekCount 指定日期所在周之前多少个周
     * @param afterWeekCount 指定日期所在周之后多少个周
     */
    operator fun invoke(
        targetDate: Date,
        beforeWeekCount: Int,
        afterWeekCount: Int,
    ): List<Date> {
        val currentWeek = weeklyDateUseCase(targetDate)
        val currentIndex = currentWeek.indexOf(targetDate)
        val beforeDateCount = beforeWeekCount * 7 + currentIndex
        val afterDateCount = afterWeekCount * 7 + (7 - currentIndex - 1)
        return rangeDateUseCase(
            targetDate,
            beforeDateCount,
            afterDateCount
        )
    }
}


