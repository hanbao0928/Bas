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

/**
 * utc 时间格式
 */
const val UTC_DATE_PATTERN = DateUtils.UTC_DATE_PATTERN

/**
 * 国内常用的完整时间格式，yyyy-MM-dd HH:mm:ss
 */
const val CN_DATE_PATTERN = DateUtils.CN_DATE_PATTERN

val CN_DATE_FORMAT = DateUtils.getCNDateFormat()


/**
 * 星期几 格式
 */
const val DATE_WEEK_FORMAT = DateUtils.WEEK_PATTERN

/**
 * utc时间格式化
 */
val UTC_DATE_FORMAT by lazy {
    DateUtils.getUTCDateFormat()
}

/**
 * 格式化时间
 */
fun Date?.format(pattern: String): String {
    return DateUtils.format(this, pattern)
}

/**
 * 获取星期几
 */
val Date.week: String
    get() =DateUtils.getWeek(this)

/**
 * utc时间格式字符串
 */
val Date.toUtc: String
    get() = DateUtils.toUTC(this)

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
