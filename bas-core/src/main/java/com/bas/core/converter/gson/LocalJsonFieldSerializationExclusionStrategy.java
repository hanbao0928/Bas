package com.bas.core.converter.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Created by Lucio on 2021/7/21.
 * Gson 字段序列化排除策略：排除[LocalJsonField]注解修饰，并且[LocalJsonField.serialize]为false的字段
 */
class LocalJsonFieldSerializationExclusionStrategy implements ExclusionStrategy {

    /**
     * @param f the field object that is under test
     * @return true if the field should be ignored; otherwise false
     */
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        LocalJsonField anno =  f.getAnnotation(LocalJsonField.class);
        return anno != null && !anno.serialize();
    }

    /**
     * @param clazz the class object that is under test
     * @return true if the class should be ignored; otherwise false
     */
    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
