package com.bas.core.lang;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

/**
 * Created by Lucio on 2021/9/9.
 */
public class NumberUtils {

    /**
     * 保留小数点后一位
     */
    private static DecimalFormat decimalFormat1;

    /**
     * 保留小数点后两位
     */
    private static DecimalFormat decimalFormat2;

    static {
        decimalFormat1 = new DecimalFormat("0.0");
        decimalFormat2 = new DecimalFormat("0.00");
    }

    /**
     * 保留一位小数
     *
     * @param value
     * @return
     */
    public static String toDecimal1(double value) {
        return decimalFormat1.format(value);
    }

    /**
     * 保留两位小数
     *
     * @param value
     * @return
     */
    public static String toDecimal2(double value) {
        return decimalFormat2.format(value);
    }

    /**
     * 保留一位小数
     *
     * @param value
     * @return
     */
    public static String toDecimal1(float value) {
        return decimalFormat1.format(value);
    }

    /**
     * 保留两位小数
     *
     * @param value
     * @return
     */
    public static String toDecimal2(float value) {
        return decimalFormat2.format(value);
    }

    /**
     * 格式化小树
     *
     * @param value        值
     * @param decimalPlaceCnt 小数位
     * @return
     */
    public static String toDecimal(double value, int decimalPlaceCnt) {
        if (decimalPlaceCnt == 0) {
            return ((int) value) + "";
        } else if (decimalPlaceCnt == 1) {
            return toDecimal1(value);
        } else if (decimalPlaceCnt == 2) {
            return toDecimal2(value);
        } else {
            return new DecimalFormat(createDecimalPattern(decimalPlaceCnt)).format(value);
        }
    }

    /**
     * 格式化小数
     *
     * @param value        值
     * @param decimalPlaceCnt 小数位
     * @param fill 填充字符
     * @param separator 分隔符
     * @return
     */
    public static String toDecimal(double value, int decimalPlaceCnt, String fill, String separator) {
        return new DecimalFormat(createDecimalPattern(decimalPlaceCnt, fill, separator)).format(value);
    }

    /**
     * 格式化小树
     *
     * @param value        值
     * @param decimalPlaceCnt 小数位
     * @return
     */
    public static String toDecimal(float value, int decimalPlaceCnt) {
        if (decimalPlaceCnt == 0) {
            return ((int) value) + "";
        } else if (decimalPlaceCnt == 1) {
            return toDecimal1(value);
        } else if (decimalPlaceCnt == 2) {
            return toDecimal2(value);
        } else {
            return new DecimalFormat(createDecimalPattern(decimalPlaceCnt)).format(value);
        }
    }

    /**
     * 格式化小数
     *
     * @param value        值
     * @param decimalPlaceCnt 小数位
     * @param fill 填充字符
     * @param separator 分隔符
     * @return
     */
    public static String toDecimal(float value, int decimalPlaceCnt, String fill, String separator) {
        return new DecimalFormat(createDecimalPattern(decimalPlaceCnt, fill, separator)).format(value);
    }

    /**
     * 创建小数格式化样式
     *
     * @param decimalPlace 小数点后位数
     * @return
     */
    public static @NotNull String createDecimalPattern(int decimalPlace) {
        return createDecimalPattern(decimalPlace, "0", ".");
    }

    /**
     * 创建小数格式化样式
     *
     * @param decimalPlaceCnt 小数位个数
     * @param fill         填充字符串,比如"0"
     * @param separator    分隔符，比如"."
     * @return
     */
    public static @NotNull String createDecimalPattern(int decimalPlaceCnt, String fill, String separator) {
        StringBuilder pattern = new StringBuilder(fill);
        if (decimalPlaceCnt > 0) {
            pattern.append(separator);
        }
        while (decimalPlaceCnt > 0) {
            decimalPlaceCnt--;
            pattern.append(fill);
        }
        return pattern.toString();
    }

//    /**
//     * 创建Decimal格式化表达式
//     * @param decimalPlace 小数点后多少位
//     * @param fill 填充字符；默认用0填充
//     */
//    internal fun createDecimalPattern(decimalPlace: Int, fill: String = "0"): String {
//        var de = decimalPlace
//        val pattern: StringBuilder = StringBuilder(fill).apply {
//            if (de > 0) {
//                append(".")
//            }
//        }
//
//        while (de > 0) {
//            de--
//            pattern.append(fill)
//        }
//        return pattern.toString()
//    }
}
