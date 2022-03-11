package bas.lib.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;

/**
 * Created by Lucio on 2022/3/10.
 */
public class ParamsUtils {

    public static TypeName arrayListTypeName(TypeName typeName) {
        return ParameterizedTypeName.get(ClassName.get(ArrayList.class), typeName);
    }

    public static TypeName stringTypeName(boolean nullable) {
        TypeName tn = TypeName.get(String.class);
        if (nullable) {
            return tn.annotated(AnnotationUtils.nullableAnnotation);
        } else {
            return tn.annotated(AnnotationUtils.nonNullAnnotation);
        }
    }

}
