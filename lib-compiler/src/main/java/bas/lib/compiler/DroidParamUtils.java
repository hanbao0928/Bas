package bas.lib.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

/**
 * Created by Lucio on 2022/3/4.
 */
public class DroidParamUtils {

    public static ClassName contextTypeName() {
        return ClassName.get("android.content", "Context");
    }

    public static ClassName attributeSetTypeName() {
        return ClassName.get("android.util", "AttributeSet");
    }

    public static ClassName viewTypeName() {
        return  ClassName.get("android.view", "View");
    }

    public static TypeName viewTypeName(boolean nullable) {
        ClassName viewClass = viewTypeName();
        if (nullable) {
            return viewClass.annotated(AnnotationUtils.nullableAnnotation);
        } else {
            return viewClass.annotated(AnnotationUtils.nonNullAnnotation);
        }
    }

    public static TypeName rectTypeName(boolean nullable) {
        ClassName viewClass = ClassName.get("android.graphics", "Rect");
        if (nullable) {
            return viewClass.annotated(AnnotationUtils.nullableAnnotation);
        } else {
            return viewClass.annotated(AnnotationUtils.nonNullAnnotation);
        }
    }

    public static TypeName canvasTypeName(boolean nullable) {
        ClassName viewClass = ClassName.get("android.graphics", "Canvas");
        if (nullable) {
            return viewClass.annotated(AnnotationUtils.nullableAnnotation);
        } else {
            return viewClass.annotated(AnnotationUtils.nonNullAnnotation);
        }
    }
}
