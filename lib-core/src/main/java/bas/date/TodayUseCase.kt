package bas.date

import java.text.SimpleDateFormat
import java.util.*

/**
 * 今日日期用例
 */
class TodayUseCase(locale: Locale = Locale.getDefault()) {
    private val pattern: String = "yyyyMMdd"

    private val formatter: SimpleDateFormat = SimpleDateFormat(pattern, locale)

    fun isToday(date: Date): Boolean {
        return formatter.format(date) == formatter.format(now())
    }
}