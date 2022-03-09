package bas.date;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import bas.lang.StringUtils;

import static bas.date.DateKt.ONE_DAY_TIME;

/**
 * Created by Lucio on 2021/8/31.
 * 日期相关工具类
 */
public class DateUtils {


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
    private static final DateFormat CN_DATETIME_FORMAT;

    private static final DateFormat UTC_DATE_FORMAT;

    static {
        UTC_DATE_FORMAT = new SimpleDateFormat(UTC_DATETIME_PATTERN);
        UTC_DATE_FORMAT.setTimeZone(getUTCTimeZone());


        CN_DATETIME_FORMAT = new SimpleDateFormat(CN_DATETIME_PATTERN);
        CN_DATETIME_FORMAT.setTimeZone(getChinaTimeZone());
    }

    @SuppressWarnings("SimpleDateFormat")
    @NotNull
    public static DateFormat getUTCDateTimeFormat() {
        return UTC_DATE_FORMAT;
    }

    public static TimeZone getUTCTimeZone() {
        return TimeZone.getTimeZone("UTC");
    }

    public static TimeZone getChinaTimeZone() {
        return TimeZone.getTimeZone("Asia/Shanghai");
    }

    @NotNull
    public static DateFormat getCNDateTimeFormat() {
        return CN_DATETIME_FORMAT;
    }

    @NotNull
    public static Date now() {
        return new Date();
    }

    /**
     * 根据{@code pattern}格式化时间
     *
     * @param date    日期
     * @param pattern 格式
     * @return 格式化后的字符串
     * @deprecated 请使用FormatDateUseCase
     */
    @SuppressWarnings("SimpleDateFormat")
    @NotNull
    @Deprecated
    public static String format(@Nullable Date date, @Nullable String pattern) {
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

    public static @NotNull
    String toUTCDateTimeFormat(@Nullable Date date) {
        return format(date, UTC_DATETIME_PATTERN);
    }

    /**
     * 获取星期
     *
     * @param date 日期
     * @return 星期几
     */
    public static @NotNull
    String getWeek(@Nullable Date date) {
        return format(date, WEEK_PATTERN);
    }

    public static @NotNull
    String toVariesTimeFormat(long timeMillis) {
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
     * @return 格式化之后的时间格式字符串
     */
    @SuppressWarnings("DefaultLocale")
    public static @NotNull
    String toVariesTimeFormat(long timeMillis, @Nullable String secondUnit) {
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


}
