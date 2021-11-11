package com.bas.core.lang

import java.util.*

/**
 * Created by Lucio on 2021/1/18.
 */

/**
 * 一天
 */
const val ONE_DAY_TIME = DateUtils.ONE_DAY_TIME

/**
 * 一个小时
 */
const val ONE_HOUR_TIME = DateUtils.ONE_HOUR_TIME

/**
 * 一分钟
 */
const val ONE_MINUTE_TIME = DateUtils.ONE_MINUTE_TIME

inline val NOW:Date get() = DateUtils.now()

/**
 * 格式化时间
 */
inline fun Date?.format(pattern: String): String {
    return DateUtils.format(this, pattern)
}

/**
 * 转换成[DateUtils.CN_DATETIME_PATTERN]时间格式
 */
inline val Date?.dateTimeFormatCN: String get() = DateUtils.toCNDateTimeFormat(this)

/**
 * 转换成日期格式
 */
inline val Date?.dateFormatCN: String get() = DateUtils.toCNDateFormat(this)

/**
 * 转换成时间格式（24hour）
 */
inline val Date?.timeFormat24: String get() = DateUtils.toTimeFormat24(this)

inline fun Long.toVariesTimeFormat(secondUnit: String = "秒"): String =
    DateUtils.toVariesTimeFormat(this, secondUnit)

/**
 * 获取星期几
 */
inline val Date.week: String
    get() = DateUtils.getWeek(this)

/**
 * utc时间格式字符串
 */
inline val Date.dateTimeFormatUTC: String
    get() = DateUtils.toUTCDateTimeFormat(this)

///**
// * 获取UTC时间
// * @return
// */
//inline fun UTCDate(): Date {
//    return DateUtils.createUTCDate()
//}
//
///**
// * 时间转换成本地时间
// */
//@Deprecated(message = "慎用，该方法是在指定时间的基础上，减去相差的时区")
//inline fun Date.toLocalDate(): Date {
//    return DateUtils.utcDateToLocalDate(this)
//}
