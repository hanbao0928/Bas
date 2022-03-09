/**
 * Created by Lucio on 2022/1/7.
 * 日期相关用例
 */
package bas.date

import java.util.*

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


