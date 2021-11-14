package com.bas.core.lang;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lucio on 2021/8/31.
 */
public class DateUtils {

    /**
     * 一天
     */
    public static final long ONE_DAY_TIME = 86400000L;

    /**
     * 一个小时
     */
    public static final long ONE_HOUR_TIME = 3600000L;

    /**
     * 一分钟
     */
    public static final long ONE_MINUTE_TIME = 60000L;

    /**
     * utc 时间格式
     */
    public static final String UTC_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * 国内常用的完整时间格式，yyyy-MM-dd HH:mm:ss
     */
    public static final String CN_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 国内常用日期格式
     */
    public static final String CN_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 国内常用时间格式
     */
    public static final String TIME24_PATTERN = "HH:mm:ss";

    /**
     * 星期几 格式
     */
    public static final String WEEK_PATTERN = "E";


    @SuppressWarnings("SimpleDateFormat")
    private static final DateFormat CN_DATETIME_FORMAT = new SimpleDateFormat(CN_DATETIME_PATTERN);


    private static DateFormat UTC_DATE_FORMAT;

    public static DateFormat getUTCDateTimeFormat() {
        if (UTC_DATE_FORMAT == null) {
            UTC_DATE_FORMAT = new SimpleDateFormat(UTC_DATETIME_PATTERN);
        }
        return UTC_DATE_FORMAT;
    }

    public static DateFormat getCNDateTimeFormat() {
        return CN_DATETIME_FORMAT;
    }

    @Contract(" -> new")
    public static @NotNull Date now() {
        return new Date();
    }

    @SuppressWarnings("SimpleDateFormat")
    @NotNull
    public static String format(@Nullable Date date, String pattern) {
        if (StringUtils.isNullOrEmpty(pattern) || date == null) return "";
        if (CN_DATETIME_PATTERN.equals(pattern)) {
            return getCNDateTimeFormat().format(date);
        } else if (UTC_DATETIME_PATTERN.equals(pattern)) {
            return getUTCDateTimeFormat().format(date);
        } else {
            return new SimpleDateFormat(pattern).format(date);
        }
    }

    /**
     * 转换成[DateUtils.CN_DATETIME_PATTERN]时间格式
     */
    @NotNull
    public static String toCNDateTimeFormat(@Nullable Date date) {
        return format(date, CN_DATETIME_PATTERN);
    }

    /**
     * 转换成日期格式
     */
    @NotNull
    public static String toCNDateFormat(@Nullable Date date) {
        return format(date, CN_DATE_PATTERN);
    }

    /**
     * 转换成时间格式（24hour）
     */
    @NotNull
    public static String toTimeFormat24(@Nullable Date date) {
        return format(date, TIME24_PATTERN);
    }

    public static @NotNull String toUTCDateTimeFormat(@Nullable Date date) {
        return format(date, UTC_DATETIME_PATTERN);
    }

    /**
     * 获取星期
     *
     * @param date
     * @return
     */
    public static @NotNull String getWeek(@Nullable Date date) {
        return format(date, WEEK_PATTERN);
    }

    public static @NotNull String toVariesTimeFormat(long timeMillis) {
        return toVariesTimeFormat(timeMillis, "秒");
    }

    /**
     * 转换成 小时、分钟、秒
     * s[secondUnit]
     * mm:ss
     * HH:mm:ss
     * yyyy-MM-dd HH:mm:ss
     *
     * @param timeMillis 毫秒
     * @param secondUnit [timeMills]计算结果在秒内时，后面拼接的单位
     * @return
     */
    public static @NotNull String toVariesTimeFormat(long timeMillis, String secondUnit) {
        if (timeMillis < ONE_MINUTE_TIME) {
            return timeMillis / 1000 + secondUnit;
        } else if (timeMillis < ONE_HOUR_TIME) {
            long minute = timeMillis / ONE_MINUTE_TIME;
            long seconds = timeMillis % ONE_MINUTE_TIME / 1000;
            return String.format("%02d:%02d", minute, seconds);
        } else if (timeMillis < ONE_DAY_TIME) {
            long hour = timeMillis / ONE_HOUR_TIME;
            long minute = timeMillis % ONE_HOUR_TIME / ONE_MINUTE_TIME;
            long seconds = timeMillis % ONE_HOUR_TIME % ONE_MINUTE_TIME / 1000;
            return String.format("%02d:%02d:%02d", hour, minute, seconds);
        } else {
            return toCNDateTimeFormat(new Date(timeMillis));
        }
    }

//    /**
//     * 创建UTC日期
//     *
//     * @return
//     */
//    public static Date createUTCDate() {
//        // 1、取得本地时间：
//        Calendar cal = Calendar.getInstance();
//        // 2、取得时间偏移量：
//        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
//        // 3、取得夏令时差：
//        int dstOffset = cal.get(Calendar.DST_OFFSET);
//        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
//        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
//        return cal.getTime();
//    }
//
//    /**
//     * UTC日期转换成本地日期
//     */
//    public static Date utcDateToLocalDate(Date date) {
//        // 1、取得本地时间：
//        Calendar cal = Calendar.getInstance();
//        // 2、取得时间偏移量：
//        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
//        // 3、取得夏令时差：
//        int dstOffset = cal.get(Calendar.DST_OFFSET);
//        cal.setTime(date);
//        cal.add(Calendar.MILLISECOND, zoneOffset + dstOffset);
//        return cal.getTime();
//    }

}
