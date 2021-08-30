package com.bas.core.lang;

/**
 * Created by Lucio on 2021/7/22.
 */
public class ClassUtils {

    public static boolean isClassExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
