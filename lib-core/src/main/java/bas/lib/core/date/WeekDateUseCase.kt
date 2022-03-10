package bas.lib.core.date

import java.util.*

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