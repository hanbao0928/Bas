/**
 * Created by Lucio on 2021/1/18.
 * 日期相关工具类
 */

@file:JvmName("DatesKt")

package bas.lib.core.date

import bas.lib.core.friendlyDateFormat
import java.util.*

/**
 * 一天
 */
const val ONE_DAY_TIME = 86400000L

/**
 * 一个小时
 */
const val ONE_HOUR_TIME = 3600000L

/**
 * 一分钟
 */
const val ONE_MINUTE_TIME = 60000L

/**
 * 当前日期
 */
inline fun now(): Date = Date()

/**
 * 当前时间
 */
inline fun nowTime(): Long = System.currentTimeMillis()

/**
 * 格式化时间
 */
inline fun Date?.format(pattern: String): String {
    return DateUtils.format(this, pattern)
}

/**
 * 转换成友好字符串显示
 * @note 接收器设置为可空的目的只是为了兼容Java可能存在传递为空对象的情况
 */
inline fun Date?.toFriendlyFormat(): String =
    if (this == null) "" else friendlyDateFormat.format(this)

/**
 * 转换成日期格式
 */
inline fun Date?.toDateFormatCN(): String = DateUtils.toCNDateFormat(this)

/**
 * 转换成[DateUtils.CN_DATETIME_PATTERN]日期时间格式
 */
@Deprecated(message = "", replaceWith = ReplaceWith("this.dateTimeFormatCN()", imports = ["bas.lib.core.date.dateTimeFormatCN"]))
inline fun Date?.toDateTimeFormatCN(): String =this.dateTimeFormatCN()


inline fun Date?.dateTimeFormatCN(): String = DateUtils.toCNDateTimeFormat(this)

/**
 * 转换成时间格式（24hour）
 */
inline fun Date?.toTimeFormat24(): String = DateUtils.toTimeFormat24(this)


inline fun Long.toVariesTimeFormat(secondUnit: String = "秒"): String =
    DateUtils.toVariesTimeFormat(this, secondUnit)

/**
 * 获取星期几
 */
inline val Date.week: String
    get() = DateUtils.getWeek(this)

/**
 * 转换成[DateUtils.UTC_DATETIME_PATTERN]时间格式
 */
inline fun Date.toDateTimeFormatUTC(): String = DateUtils.toUTCDateTimeFormat(this)
