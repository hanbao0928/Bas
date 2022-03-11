package bas.lib.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

/**
 * Created by Lucio on 2022/3/10.
 */
public class AnnotationUtils {

    public static final AnnotationSpec nullableAnnotation = AnnotationSpec.builder(
            ClassName.get("androidx.annotation", "Nullable")
    ).build();

    public static final AnnotationSpec nonNullAnnotation = AnnotationSpec.builder(
            ClassName.get("androidx.annotation", "NonNull")
    ).build();

    public static final AnnotationSpec pxAnnotation = AnnotationSpec.builder(
            ClassName.get("androidx.annotation", "Px")
    ).build();

    public static final AnnotationSpec colorIntAnnotation = AnnotationSpec.builder(
            ClassName.get("androidx.annotation", "ColorInt")
    ).build();


    public static AnnotationSpec createIntRangeAnnotation(int from, int to) {
        return AnnotationSpec.builder(ClassName.get("androidx.annotation", "IntRange"))
                .addMember("from", String.valueOf(from))
                .addMember("to", String.valueOf(to)).build();
    }

    public static AnnotationSpec createSuppressLintAnnotation(String value) {
        return AnnotationSpec.builder(ClassName.get("android.annotation", "SuppressLint"))
                .addMember("value", "value = \"" + value + "\"")
                .build();
    }

    public static AnnotationSpec createMissingSuperCallAnnotation() {
        return createSuppressLintAnnotation("MissingSuperCall");
    }
}
