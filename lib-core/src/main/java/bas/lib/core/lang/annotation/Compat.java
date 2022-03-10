package bas.lib.core.lang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Lucio on 2021/7/27.
 * 兼容代码
 */
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface Compat {
    String message() default "";
}
