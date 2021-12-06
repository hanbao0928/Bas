package com.bas.core.lang;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Created by Lucio on 2021/12/5.
 * 正则相关
 */
public class RegularUtils {

    /**
     * 是否是版本数字；eg. like 1 or 1.0  or 3.1.25
     */
    public static boolean isVersionNumber(@NotNull String value){
        Pattern pattern = Pattern.compile("[\\d.]+");
        return pattern.matcher(value).matches();
    }


}
