package bas.leanback.layout.annotation;

import android.view.ViewGroup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Lucio on 2021/11/29.
 * 在TV上进行布局的控件（针对ViewGroup）：LinearLayout、ConstraintLayout等
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface LeanbackLayout {
    Class<? extends ViewGroup>[] value() default {};
}
