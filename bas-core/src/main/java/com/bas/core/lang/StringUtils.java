package com.bas.core.lang;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Created by Lucio on 2021/7/21.
 * 字符串相关工具类
 */
public class StringUtils {

    /**
     * 判断是否为null或内容为空
     *
     * @return 如果{@code str}为null或内容为空，则返回true，其他情况返回false
     */
    public static boolean isNullOrEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 为空（=null或内容为空）时返回默认值
     *
     * @param str 字符串
     * @param def 默认值
     * @return {@code str}=null 或 内容为空时，返回{@code def},否则返回{@code str}.
     */
    @NotNull
    public static String orDefaultIfNullOrEmpty(@Nullable String str, @NotNull String def) {
        Objects.requireNonNull(def);
        return isNullOrEmpty(str) ? def : str;
    }

    public static int toIntOrDefault(@Nullable String str) {
        return toIntOrDefault(str, 0);
    }

    public static int toIntOrDefault(@Nullable String str, int def) {
        try {
            if (StringUtils.isNullOrEmpty(str))
                return def;
            return Integer.parseInt(str);
        } catch (Exception e) {
            return def;
        }
    }

    public static long toLongOrDefault(@Nullable String str) {
        return toLongOrDefault(str, 0);
    }

    public static long toLongOrDefault(@Nullable String str, long def) {
        try {
            if (StringUtils.isNullOrEmpty(str))
                return def;
            return Long.parseLong(str);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 获取字符串扩展名
     * @see #getExtension(String, String, String)
     */
    public static String getExtension(@NotNull String value) {
        return getExtension(value, ".", value);
    }

    /**
     * 获取字符串扩展名
     * @see #getExtension(String, String, String)
     */
    public static String getExtension(@NotNull String value, @NotNull String delimiter) {
        return getExtension(value, delimiter, value);
    }

    /**
     * 获取扩展名（Returns the extension of this String (not including the [delimiter]), or a [defValue] string if it doesn't have one.）
     *
     * @param delimiter             扩展符号
     * @param missingDelimiterValue 默认值；未找到扩展符号时使用的默认值
     * @return 返回字符串的扩展名（不包括扩展符[delimiter]）,或者字符串不包含扩展符时返回[missingDelimiterValue]
     */
    public static String getExtension(@NotNull String value, @NotNull String delimiter, @NotNull String missingDelimiterValue) {
        int index = value.indexOf(delimiter);
        if (index == -1) {
            return missingDelimiterValue;
        } else {
            return value.substring(index + delimiter.length());
        }
    }


    /**
     * 判断两个字符串内容是否相同
     *
     * @return 如果两者内容相同（同为null或内容相同），则返回true，其他情况返回false
     */
    public static boolean areContentsSame(@Nullable CharSequence str1, @Nullable CharSequence str2) {
        if ((str1 == null) != (str2 == null)) {
            //其中一个为null，一个不为null
            return false;
        }

        //同时为 null 或同时不为 null
        if (str1 == null)//均为null
            return true;

        //说明两个均不为null的情况
        final int length = str1.length();
        if (length != str2.length()) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 两个内容是否不同
     *
     * @param str1 内容1
     * @param str2 内容2
     * @return 当str1的内容与str2的内容相同时返回true，其他情况则为false。
     * @deprecated 该方法逻辑没任何问题，只是方法名不如{@link #areContentsSame(CharSequence, CharSequence)},
     * 如果{@link #areContentsSame(CharSequence, CharSequence)}逻辑（参照本方法而来）无任何问题，则可以去掉该方法
     */
    @Deprecated
    public static boolean areContentsChanged(@Nullable CharSequence str1, @Nullable CharSequence str2) {
        if ((str1 == null) != (str2 == null)) {
            //str1和str2中有一个为null，一个不为null，结果必然发生变化
            return true;
        }

        //接下来的逻辑，要嘛两个均为null，要嘛两个均不为null
        if (str1 == null) {
            //说明两个均为null
            return false;
        }
        //说明两个均不为null的情况
        final int length = str1.length();
        if (length != str2.length()) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 转换为首字母大写
     */
    @NotNull
    public static String toFirstLetterUppercase(@Nullable String value) {
        if (isNullOrEmpty(value))
            return "";
        char fistChar = value.charAt(0);
        if (!Character.isLetter(fistChar) || Character.isUpperCase(fistChar)) {
            return value;
        }
        //如果第一个字符不是字母或者已经是大写字母，则返回原字符串
        return Character.toUpperCase(fistChar) + value.substring(1);
    }

    /**
     * 全拼转半拼(全角转半角)
     * toHalfWidth("") = "";
     * toHalfWidth(new String(new char[] {12288})) = " ";
     * toHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     *
     * @return 半拼字符串
     */
    @NotNull
    public static String toHalfWidth(@Nullable String value) {
        if (isNullOrEmpty(value)) {
            return "";
        }
        char[] source = value.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
            } else if (source[i] >= ' ' && source[i] <= 65374) {
                source[i] = (char) (source[i] - 65248);
            }
        }
        return new String(source);
    }

    /**
     * 转换成全拼（全角）
     * toFullWidth("") = "";
     * toFullWidth(" ") = new String(new char[] {12288});
     * toFullWidth("!\"#$%&) = "！＂＃＄％＆";
     *
     * @return 全拼字符串
     */
    @NotNull
    public static String toFullWidth(String value) {
        if (isNullOrEmpty(value)) {
            return "";
        }
        char[] source = value.toCharArray();

        //full blank space is 12288, half blank space is 32
        //others :full is 65281-65374,and half is 33-126.
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 32) {
                source[i] = (char) 12288;
            } else if (source[i] < 127) {
                source[i] = (char) (source[i] + 65248);
            }
        }
        return new String(source);
    }

}
