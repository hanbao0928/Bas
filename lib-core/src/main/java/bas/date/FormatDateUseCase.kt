package bas.date

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