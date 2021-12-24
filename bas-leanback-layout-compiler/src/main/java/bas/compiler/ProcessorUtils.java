package bas.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * Created by Lucio on 2021/11/30.
 */
public class ProcessorUtils {

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

    public static ClassName createContext() {
        return ClassName.get("android.content", "Context");
    }

    public static ClassName createAttributeSet() {
        return ClassName.get("android.util", "AttributeSet");
    }

    public static TypeName createViewTypeName() {
        return ClassName.get("android.view", "View");
    }

    public static TypeName createRectTypeName(boolean nullable) {
        ClassName viewClass = ClassName.get("android.graphics", "Rect");
        if (nullable) {
            return viewClass.annotated(nullableAnnotation);
        } else {
            return viewClass.annotated(nonNullAnnotation);
        }
    }

    public static TypeName createViewTypeName(boolean nullable) {
        ClassName viewClass = ClassName.get("android.view", "View");
        if (nullable) {
            return viewClass.annotated(nullableAnnotation);
        } else {
            return viewClass.annotated(nonNullAnnotation);
        }
    }

    public static TypeName createString(boolean nullable) {
        TypeName tn = TypeName.get(String.class);
        if (nullable) {
            return tn.annotated(nullableAnnotation);
        } else {
            return tn.annotated(nonNullAnnotation);
        }
    }

    public static TypeName createCanvasTypeName(boolean nullable) {
        ClassName viewClass = ClassName.get("android.graphics", "Canvas");
        if (nullable) {
            return viewClass.annotated(nullableAnnotation);
        } else {
            return viewClass.annotated(nonNullAnnotation);
        }
    }

    public static TypeName createArrayList(TypeName typeName) {
        return ParameterizedTypeName.get(ClassName.get(ArrayList.class), typeName);
    }

    public static boolean isAssignable(ProcessingEnvironment processingEnv, String childClazz, String superClazz) {
        return processingEnv.getTypeUtils().isAssignable(
                processingEnv.getElementUtils().getTypeElement(childClazz).asType(),
                processingEnv.getElementUtils().getTypeElement(superClazz).asType());

    }

}
