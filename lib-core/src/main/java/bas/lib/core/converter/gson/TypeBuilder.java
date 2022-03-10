package bas.lib.core.converter.gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 参考来源：https://github.com/ikidou/TypeBuilder
 * 用法：
 * public static <T> Result<List<T>> fromJsonArray(Reader reader, Class<T> clazz) {
 *     Type type = TypeBuilder
 *             .newInstance(Result.class)
 *             .beginSubType(List.class)
 *             .addTypeParam(clazz)
 *             .endSubType()
 *             .build();
 *     return GSON.fromJson(reader, type);
 * }
 *
 * public static <T> Result<T> fromJsonObject(Reader reader, Class<T> clazz) {
 *     Type type = TypeBuilder
 *             .newInstance(Result.class)
 *             .addTypeParam(clazz)
 *             .build();
 *     return GSON.fromJson(reader, type);
 * }
 *
 * 参考资料：https://www.jianshu.com/p/d62c2be60617
 */
public class TypeBuilder {

    private final TypeBuilder parent;
    private final Class<?> raw;
    private final List<Type> args = new ArrayList<>();


    private TypeBuilder(Class<?> raw, TypeBuilder parent) {
        assert raw != null;
        this.raw = raw;
        this.parent = parent;
    }

    public static TypeBuilder newInstance(Class<?> raw) {
        return new TypeBuilder(raw, null);
    }

    private static TypeBuilder newInstance(Class<?> raw, TypeBuilder parent) {
        return new TypeBuilder(raw, parent);
    }

    public TypeBuilder beginSubType(Class<?> raw) {
        return newInstance(raw, this);
    }

    public TypeBuilder endSubType() {
        if (parent == null) {
            throw new IllegalStateException("expect beginSubType() before endSubType()");
        }
        parent.addTypeParam(getType());
        return parent;
    }

    public TypeBuilder addTypeParam(Class<?> clazz) {
        return addTypeParam((Type) clazz);
    }

    public TypeBuilder addTypeParamExtends(Class<?>... classes) {
        if (classes == null) {
            throw new NullPointerException("addTypeParamExtends() expect not null Class");
        }

        WildcardTypeImpl wildcardType = new WildcardTypeImpl(null, classes);

        return addTypeParam(wildcardType);
    }

    public TypeBuilder addTypeParamSuper(Class<?>... classes) {
        if (classes == null) {
            throw new NullPointerException("addTypeParamSuper() expect not null Class");
        }

        WildcardTypeImpl wildcardType = new WildcardTypeImpl(classes, null);

        return addTypeParam(wildcardType);
    }

    public TypeBuilder addTypeParam(Type type) {
        if (type == null) {
            throw new NullPointerException("addTypeParam expect not null Type");
        }

        args.add(type);

        return this;
    }

    public Type build() {
        if (parent != null) {
            throw new IllegalStateException("expect endSubType() before build()");
        }

        return getType();
    }

    private Type getType() {
        if (args.isEmpty()) {
            return raw;
        }
        return new ParameterizedTypeImpl(raw,args.toArray(new Type[args.size()]), null);
    }
}