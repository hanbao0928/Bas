package bas.lib.compiler;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

/**
 * Created by Lucio on 2022/3/4.
 */
public class DroidMethodUtils {

    /**
     * 添加重载构造函数
     * 注意：构造函数中默认样式有些时候需要指定，请注意校验该参数
     *
     * @param typeBuilder
     * @param clazz
     * @param constructorCode
     */
    public static void viewConstructorOverloads(TypeSpec.Builder typeBuilder, String clazz, CodeBlock constructorCode) {
        TypeName contextType = DroidParamUtils.contextTypeName()
                .annotated(AnnotationUtils.nonNullAnnotation);
        TypeName attributeSetType = DroidParamUtils.attributeSetTypeName()
                .annotated(AnnotationUtils.nullableAnnotation);

        MethodSpec constructorOne = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(contextType, "context")
                .addStatement("this(context,(AttributeSet) null)")
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

    public static MethodSpec.Builder onSizeChangedSignature() {
        return MethodSpec.methodBuilder("onSizeChanged")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .addParameter(int.class, "w")
                .addParameter(int.class, "h")
                .addParameter(int.class, "oldw")
                .addParameter(int.class, "oldh");
    }

    public static MethodSpec.Builder onDrawSignature() {
        return MethodSpec.methodBuilder("onDraw")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .addParameter(DroidParamUtils.canvasTypeName(true), "canvas");
    }

    public static MethodSpec.Builder onRequestFocusInDescendantsSignature() {
        return MethodSpec.methodBuilder("onRequestFocusInDescendants")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .addParameter(int.class, "direction")
                .addParameter(DroidParamUtils.rectTypeName(true), "previouslyFocusedRect");
    }

    public static MethodSpec.Builder onViewRemovedSignature() {
        return MethodSpec.methodBuilder("onViewRemoved")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(DroidParamUtils.viewTypeName(true), "child");
    }

    public static MethodSpec.Builder addFocusablesSignature() {
        return MethodSpec.methodBuilder("addFocusables")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParamsUtils.arrayListTypeName(DroidParamUtils.viewTypeName()), "views")
                .addParameter(int.class, "direction")
                .addParameter(int.class, "focusableMode");
    }

    public static MethodSpec.Builder requestChildFocusSignature() {
        return MethodSpec.methodBuilder("requestChildFocus")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(DroidParamUtils.viewTypeName(true), "child")
                .addParameter(DroidParamUtils.viewTypeName(true), "focused");
    }

    public static MethodSpec.Builder isChildrenDrawingOrderEnabledSignature() {
        return MethodSpec.methodBuilder("isChildrenDrawingOrderEnabled")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);
    }

    public static MethodSpec.Builder getChildDrawingOrderSignature() {
        return MethodSpec.methodBuilder("getChildDrawingOrder")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .addParameter(int.class, "childCount")
                .addParameter(int.class, "drawingPosition");
    }

    public static MethodSpec.Builder bringChildToFrontSignature() {
        return MethodSpec.methodBuilder("bringChildToFront")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(DroidParamUtils.viewTypeName(true), "child");
    }

}
