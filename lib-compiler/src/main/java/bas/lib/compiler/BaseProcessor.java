package bas.lib.compiler;

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
