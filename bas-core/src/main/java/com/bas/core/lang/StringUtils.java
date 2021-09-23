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
}
