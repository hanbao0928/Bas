package com.bas.core.converter.gson;

/**
 * Created by Lucio on 2021/7/21.
 * api序列化忽略的字段注解
 * 用法:
 * @LocalJsonField(serialize = false,deserialize = false)
 * String name = ""
 */
public @interface LocalJsonField {
    /**
     * If `true`, the field marked with this annotation is written out in the JSON while
     * serializing. If `false`, the field marked with this annotation is skipped from the
     * serialized output. Defaults to `false`.
     * @since 1.4
     */
    boolean serialize()  default false;
    /**
     * If `true`, the field marked with this annotation is deserialized from the JSON.
     * If `false`, the field marked with this annotation is skipped during deserialization.
     * Defaults to `false`.
     * @since 1.4
     */
    boolean deserialize() default false;
}
