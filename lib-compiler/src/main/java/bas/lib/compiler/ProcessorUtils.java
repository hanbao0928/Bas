package bas.lib.compiler;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * Created by Lucio on 2021/11/30.
 */
public class ProcessorUtils {

    public static boolean isAssignable(ProcessingEnvironment processingEnv, String childClazz, String superClazz) {
        return processingEnv.getTypeUtils().isAssignable(
                processingEnv.getElementUtils().getTypeElement(childClazz).asType(),
                processingEnv.getElementUtils().getTypeElement(superClazz).asType());

    }

}
