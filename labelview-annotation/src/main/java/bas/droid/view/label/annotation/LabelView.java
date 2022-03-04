package bas.droid.view.label.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Lucio on 2021/11/29.
 * 在View四个角落绘制文本标签
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface LabelView {
    Class<? extends View>[] value() default {};
}
