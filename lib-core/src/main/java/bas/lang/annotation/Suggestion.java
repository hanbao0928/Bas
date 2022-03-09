package bas.lang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Lucio on 2021/7/27.
 * 建议
 */
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface Suggestion {
    String message() default "";
}
