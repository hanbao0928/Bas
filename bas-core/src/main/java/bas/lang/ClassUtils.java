package bas.lang;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Lucio on 2021/7/22.
 * 类工具类
 */
public class ClassUtils {

    public static boolean isClassExists(@NotNull String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
