package bas.lib.compiler;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

/**
 * Created by Lucio on 2022/3/4.
 */
public class MethodUtils {

    /**
     * 添加重载构造函数
     * 注意：构造函数中默认样式有些时候需要指定，请注意校验该参数
     *
     * @param typeBuilder
     * @param clazz
     * @param constructorCode
     */
    public static void constructorOverloads(TypeSpec.Builder typeBuilder, String clazz, CodeBlock constructorCode) {
        TypeName contextType = ProcessorUtils.createContext()
                .annotated(ProcessorUtils.nonNullAnnotation);
        TypeName attributeSetType = ProcessorUtils.createAttributeSet()
                .annotated(ProcessorUtils.nullableAnnotation);

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
                .addParameter(ProcessorUtils.createCanvasTypeName(true), "canvas");
    }

}
