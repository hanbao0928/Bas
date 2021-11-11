package com.bas.core.lang;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Created by Lucio on 2021/7/21.
 */
public class StringUtils {

    public static boolean isNullOrEmpty(@Nullable String str) {
        return str == null || str.length() == 0;
    }

    @NotNull
    public static String orDefaultIfNullOrEmpty(@Nullable String str, @NotNull String def) {
        Objects.requireNonNull(def);
        return isNullOrEmpty(str) ? def : str;
    }

    public static int toIntOrDefault(String str) {
        return toIntOrDefault(str,0);
    }

    public static int toIntOrDefault(String str, int def) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return def;
        }
    }

    public static long toLongOrDefault(String str) {
        return toLongOrDefault(str,0);
    }

    public static long toLongOrDefault(String str, long def) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return def;
        }
    }
}
