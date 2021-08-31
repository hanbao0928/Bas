package com.bas.core.lang;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    public static final String UTC_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * 国内常用的完整时间格式，yyyy-MM-dd HH:mm:ss
     */
    public static final String CN_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 星期几 格式
     */
    public static final String WEEK_PATTERN = "E";


    @SuppressWarnings("SimpleDateFormat")
    private static final DateFormat CN_DATE_FORMAT = new SimpleDateFormat(CN_DATE_PATTERN);


    private static DateFormat UTC_DATE_FORMAT;

    public static DateFormat getUTCDateFormat() {
        if (UTC_DATE_FORMAT == null) {
            UTC_DATE_FORMAT = new SimpleDateFormat(UTC_DATE_PATTERN);
        }
        return UTC_DATE_FORMAT;
    }

    public static DateFormat getCNDateFormat() {
        return CN_DATE_FORMAT;
    }

    @SuppressWarnings("SimpleDateFormat")
    @NotNull
    public static String format(Date date, String pattern) {
        if (StringUtils.isNullOrEmpty(pattern) || date == null) return "";

        if (CN_DATE_PATTERN.equals(pattern)) {
            return getCNDateFormat().format(pattern);
        } else if (UTC_DATE_PATTERN.equals(pattern)) {
            return getUTCDateFormat().format(pattern);
        } else {
            return new SimpleDateFormat(pattern).format(date);
        }
    }

    public static String toUTC(Date date) {
        return format(date, UTC_DATE_PATTERN);
    }

    public static String getWeek(Date date) {
        return format(date, WEEK_PATTERN);
    }

    /**
     * 创建UTC日期
     *
     * @return
     */
    public static Date createUTCDate() {
        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance();
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return cal.getTime();
    }

    /**
     * UTC日期转换成本地日期
     */
    public static Date utcDateToLocalDate(Date date) {
        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance();
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        cal.setTime(date);
        cal.add(Calendar.MILLISECOND, zoneOffset + dstOffset);
        return cal.getTime();
    }

}
