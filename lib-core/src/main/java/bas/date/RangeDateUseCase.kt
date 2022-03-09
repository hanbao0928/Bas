package bas.date

import java.util.*

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
        repeat(beforeDateCount) {
            items.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
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