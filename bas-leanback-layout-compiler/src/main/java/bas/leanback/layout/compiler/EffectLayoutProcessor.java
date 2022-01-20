package bas.leanback.layout.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;

import bas.compiler.BaseProcessor;
import bas.compiler.ProcessorUtils;
import bas.leanback.layout.annotation.EffectLayout;

/**
 * Created by Lucio on 2021/12/9.
 */
@AutoService(Processor.class)
public class EffectLayoutProcessor extends BaseProcessor {
    private static final String TAG = "EffectLayoutProcessor";

    private static final String CLASS_JAVA_DOC = "Generated by bas-leanback-layout-compiler. Do not edit it!\n";
    private static final String PACKAGE_NAME = "bas.leanback.layout";
    private static final String CLASS_PREFIX = "Effect";

    @Override
    protected String getTag() {
        return TAG;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        final Set<String> annotationTypes = new LinkedHashSet<>();
        annotationTypes.add(EffectLayout.class.getCanonicalName());
        return annotationTypes;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(EffectLayout.class);
        if (elements == null || elements.isEmpty()) {
            return true;
        }
        printDividerMessage(TAG + " process START");
        Set<String> viewClassSet = new HashSet<>();
        parseParams(elements, viewClassSet);
        try {
            generateClasses(viewClassSet);
        } catch (IllegalAccessException e) {
            printErrorMessage("IllegalAccessException occurred when generating class file.");
            e.printStackTrace();
        } catch (IOException e) {
            printErrorMessage("IOException occurred when generating class file.");
            e.printStackTrace();
        }
        printDividerMessage(TAG + " process END");
        return true;
    }

    private void generateClasses(Set<String> viewClassSet) throws IllegalAccessException, IOException {
        printMessage(String.format("准备生成%d个文件", viewClassSet.size()));
        for (String clazz : viewClassSet) {
            int lastDotIndex = clazz.lastIndexOf(".");
            String superPackageName = clazz.substring(0, lastDotIndex);
            String superClassName = clazz.substring(lastDotIndex + 1);
            if ("LinearLayout".equalsIgnoreCase(superClassName)) {
                printWarnMessage("不支持LinearLayout，跳过该文件生成：" + clazz);
                continue;
            }
            String className = CLASS_PREFIX + superClassName;

            printMessage(String.format("正在生成 %s ====>  %s", clazz, className));

            TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(className)
                    .addJavadoc(CLASS_JAVA_DOC)
                    .addModifiers(Modifier.PUBLIC)
                    .superclass(ClassName.get(superPackageName, superClassName))
                    .addSuperinterface(ClassName.get(PACKAGE_NAME, "EffectLayoutDelegate.Callback"))
                    .addField(ClassName.get(PACKAGE_NAME, "EffectLayoutDelegate"), "effectDelegate", Modifier.PRIVATE, Modifier.FINAL);

            generateMethods(typeBuilder, clazz);

            JavaFile javaFile = JavaFile.builder(PACKAGE_NAME, typeBuilder.build()).build();
            javaFile.writeTo(filer);
        }
    }

    private void generateMethods(TypeSpec.Builder typeBuilder, String clazz) {
        viewConstructor(typeBuilder, clazz, constructorCode());
        /*额外新增的构造函数：用于直接创建Layout的场景*/
        TypeName contextType = ProcessorUtils.createContext()
                .annotated(ProcessorUtils.nonNullAnnotation);
        MethodSpec extraConstructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(contextType, "context")
                .addParameter(ClassName.get(PACKAGE_NAME,"EffectParams").annotated(ProcessorUtils.nonNullAnnotation), "effectParams")
                .addStatement("super(context)")
                .addStatement("effectDelegate = EffectLayoutDelegate.create(this, this, effectParams)")
                .build();
        typeBuilder.addMethod(extraConstructor);
                
        delegateCallbackImpl(typeBuilder, clazz);
        onSizeChanged(typeBuilder, clazz);
        dispatchDraw(typeBuilder, clazz);
        draw(typeBuilder, clazz);
        onFocusChanged(typeBuilder, clazz);
        performFocusChanged(typeBuilder, clazz);
        onViewAdded(typeBuilder, clazz);
        onDetachedFromWindow(typeBuilder, clazz);
    }


    private CodeBlock constructorCode() {
        return CodeBlock.builder()
                .addStatement("super(context, attrs, defStyleAttr)")
                .addStatement("effectDelegate = EffectLayoutDelegate.create(this, this, attrs, defStyleAttr)")
                .build();
    }

    private void delegateCallbackImpl(TypeSpec.Builder typeBuilder, String clazz) {
        MethodSpec callSuperDispatchDraw = MethodSpec.methodBuilder("callSuperDispatchDraw")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ProcessorUtils.createCanvasTypeName(true), "canvas")
                .addStatement("super.dispatchDraw(canvas)")
                .build();
        typeBuilder.addMethod(callSuperDispatchDraw);

        MethodSpec callSuperDraw = MethodSpec.methodBuilder("callSuperDraw")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ProcessorUtils.createCanvasTypeName(true), "canvas")
                .addStatement("super.draw(canvas)")
                .build();
        typeBuilder.addMethod(callSuperDraw);

        MethodSpec callSuperOnDetachedFromWindow = MethodSpec.methodBuilder("callSuperOnDetachedFromWindow")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super.onDetachedFromWindow()")
                .build();
        typeBuilder.addMethod(callSuperOnDetachedFromWindow);
    }

    private void onSizeChanged(TypeSpec.Builder typeBuilder, String clazz) {
        MethodSpec onSizeChanged = MethodSpec.methodBuilder("onSizeChanged")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .addParameter(int.class, "w")
                .addParameter(int.class, "h")
                .addParameter(int.class, "oldw")
                .addParameter(int.class, "oldh")
                .addStatement("super.onSizeChanged(w, h, oldw, oldh)")
                .addStatement("effectDelegate.onSizeChanged(w, h, oldw, oldh)")
                .build();
        typeBuilder.addMethod(onSizeChanged);
    }

    private void dispatchDraw(TypeSpec.Builder typeBuilder, String clazz) {
        MethodSpec dispatchDraw = MethodSpec.methodBuilder("dispatchDraw")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .addParameter(ProcessorUtils.createCanvasTypeName(true), "canvas")
                .addStatement("assert canvas != null")
                .addStatement("effectDelegate.dispatchDraw(canvas)")
                .build();
        typeBuilder.addMethod(dispatchDraw);
    }

    private void draw(TypeSpec.Builder typeBuilder, String clazz) {
        MethodSpec draw = MethodSpec.methodBuilder("draw")
                .addAnnotation(ProcessorUtils.createMissingSuperCallAnnotation())
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ProcessorUtils.createCanvasTypeName(true), "canvas")
                .addStatement("assert canvas != null")
                .addStatement("effectDelegate.draw(canvas)")
                .build();
        typeBuilder.addMethod(draw);
    }

    private void onFocusChanged(TypeSpec.Builder typeBuilder, String clazz) {
        MethodSpec onFocusChanged = MethodSpec.methodBuilder("onFocusChanged")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .addParameter(boolean.class, "gainFocus")
                .addParameter(int.class, "direction")
                .addParameter(ProcessorUtils.createRectTypeName(true), "previouslyFocusedRect")
                .addStatement("super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)")
                .addStatement("effectDelegate.onFocusChanged(gainFocus, direction, previouslyFocusedRect)")
                .build();
        typeBuilder.addMethod(onFocusChanged);
    }

    private void performFocusChanged(TypeSpec.Builder typeBuilder, String clazz) {
        MethodSpec performFocusChanged = MethodSpec.methodBuilder("performFocusChanged")
                .addJavadoc("执行焦点效果")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(boolean.class, "hasFocus")
                .addStatement("effectDelegate.performFocusChanged(hasFocus)")
                .build();
        typeBuilder.addMethod(performFocusChanged);
    }

    private void onViewAdded(TypeSpec.Builder typeBuilder, String clazz) {
        MethodSpec onViewAdded = MethodSpec.methodBuilder("onViewAdded")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ProcessorUtils.createViewTypeName(true), "child")
                .addStatement("super.onViewAdded(child)")
                .addStatement("effectDelegate.onViewAdded(child)")
                .build();
        typeBuilder.addMethod(onViewAdded);
    }

    private void onDetachedFromWindow(TypeSpec.Builder typeBuilder, String clazz) {
        MethodSpec onDetachedFromWindow = MethodSpec.methodBuilder("onDetachedFromWindow")
                .addAnnotation(ProcessorUtils.createMissingSuperCallAnnotation())
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .addStatement("effectDelegate.onDetachedFromWindow()")
                .build();
        typeBuilder.addMethod(onDetachedFromWindow);
    }

    private void parseParams(Set<? extends Element> elements, Set<String> viewClassSet) {
        for (Element element : elements) {
            checkAnnotationValid(element, EffectLayout.class);
            TypeElement classElement = (TypeElement) element;
            // 获取该注解的值
            EffectLayout badgeAnnotation = classElement.getAnnotation(EffectLayout.class);
            try {
                badgeAnnotation.value();
            } catch (MirroredTypesException e) {
                List<? extends TypeMirror> typeMirrors = e.getTypeMirrors();
                for (TypeMirror typeMirror : typeMirrors) {
                    DeclaredType classTypeMirror = (DeclaredType) typeMirror;
                    TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
                    String qualifiedName = classTypeElement.getQualifiedName().toString();
                    viewClassSet.add(qualifiedName);
                }
            }
        }
    }
}
