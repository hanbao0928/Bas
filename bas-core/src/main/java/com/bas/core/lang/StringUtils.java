package com.bas.core.lang;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Created by Lucio on 2021/7/21.
 */
public class StringUtils {

    /**
     * 内容是否相同
     *
     * @param str1
     * @param str2
     * @return
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

//    /**
//     * 两个内容是否不同
//     *
//     * @param str1 内容1
//     * @param str2 内容2
//     * @return 当str1的内容与str2的内容相同时返回true，其他情况则为false。
//     */
//    public static boolean areContentsChanged(@Nullable CharSequence str1, @Nullable CharSequence str2) {
//        if ((str1 == null) != (str2 == null)) {
//            //str1和str2中有一个为null，一个不为null，结果必然发生变化
//            return true;
//        }
//
//        //接下来的逻辑，要嘛两个均为null，要嘛两个均不为null
//        if (str1 == null) {
//            //说明两个均为null
//            return false;
//        }
//        //说明两个均不为null的情况
//        final int length = str1.length();
//        if (length != str2.length()) {
//            return true;
//        }
//        for (int i = 0; i < length; i++) {
//            if (str1.charAt(i) != str2.charAt(i)) {
//                return true;
//            }
//        }
//        return false;
//    }

    public static boolean isNullOrEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    @NotNull
    public static String orDefaultIfNullOrEmpty(@Nullable String str, @NotNull String def) {
        Objects.requireNonNull(def);
        return isNullOrEmpty(str) ? def : str;
    }

    public static int toIntOrDefault(String str) {
        return toIntOrDefault(str, 0);
    }

    public static int toIntOrDefault(String str, int def) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return def;
        }
    }

    public static long toLongOrDefault(String str) {
        return toLongOrDefault(str, 0);
    }

    public static long toLongOrDefault(String str, long def) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return def;
        }
    }


}
