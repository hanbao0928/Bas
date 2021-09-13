package com.bas.core.lang;

import java.text.DecimalFormat;

/**
 * Created by Lucio on 2021/9/9.
 */
public class NumberUtils {

    /**
     * 保留小数点后一位
     */
    private static DecimalFormat mDecimalFormat1;

    /**
     * 保留小数点后两位
     */
    private static DecimalFormat mDecimalFormat2;

    static {
        mDecimalFormat1 = new DecimalFormat("0.0");
        mDecimalFormat2 = new DecimalFormat("0.00");
    }
//
//    internal val decimalFormat1 by lazy {
//        DecimalFormat("0.0")
//    }
//
//    internal val decimalFormat2 by lazy {
//        DecimalFormat("0.00")
//    }
//
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
