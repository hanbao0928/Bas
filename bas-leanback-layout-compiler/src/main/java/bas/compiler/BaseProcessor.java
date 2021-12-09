package bas.compiler;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by Lucio on 2021/12/9.
 */
public abstract class BaseProcessor extends AbstractProcessor {

    protected Filer filer;
    protected Elements elementUtils;
    protected Messager messager;

    protected abstract String getTag();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnv.getFiler();
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        printMessage("init success");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    protected void printMessage(String message) {
        messager.printMessage(Diagnostic.Kind.NOTE, String.format("\t[%s]:%s\r\n", getTag(), message));
    }

    protected void printErrorMessage(String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, "\t" + message + "\r\n");
    }

    protected void printWarnMessage(String message) {
        messager.printMessage(Diagnostic.Kind.WARNING, "\t" + message + "\r\n");
    }

    protected void printErrorMessage(Element element, String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, "\t" + message + "\r\n", element);
    }

    protected void printDividerMessage(String title) {
        printMessage(String.format("====================================== %s ======================================", title));
    }

    /**
     * 生成View常用的构造函数
     * @param typeBuilder
     * @param clazz
     * @param constructorCode
     */
    protected void viewConstructor(TypeSpec.Builder typeBuilder, String clazz, CodeBlock constructorCode) {
        TypeName contextType = ProcessorUtils.createContext()
                .annotated(ProcessorUtils.nonNullAnnotation);
        TypeName attributeSetType = ProcessorUtils.createAttributeSet()
                .annotated(ProcessorUtils.nullableAnnotation);

        MethodSpec constructorOne = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(contextType, "context")
                .addStatement("this(context, null)")
                .build();
        MethodSpec constructorTwo = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(contextType, "context")
                .addParameter(attributeSetType, "attrs")
                .addStatement("this(context, attrs, 0)")
                .build();
        MethodSpec.Builder constructorThreeBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(contextType, "context")
                .addParameter(attributeSetType, "attrs")
                .addParameter(int.class, "defStyleAttr")
                .addCode(constructorCode);
        typeBuilder.addMethod(constructorOne)
                .addMethod(constructorTwo)
                .addMethod(constructorThreeBuilder.build());
    }

    protected boolean checkAnnotationValid(Element annotatedElement, Class clazz) {
        if (annotatedElement.getKind() != ElementKind.CLASS) {
            printErrorMessage(annotatedElement, String.format("%s must be declared on class.", clazz.getSimpleName()));
            return false;
        }

        if (annotatedElement.getModifiers().contains(Modifier.PRIVATE)) {
            printErrorMessage(annotatedElement, String.format("%s must can not be private.", ((TypeElement) annotatedElement).getQualifiedName()));
            return false;
        }
        return true;
    }

}
