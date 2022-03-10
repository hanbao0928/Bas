package bas.lib.core.converter.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Created by Lucio on 2021/7/21.
 * Gson字段反序列化排除策略：排除[LocalJsonField]注解修饰，并且[LocalJsonField.serialize]为false的字段
 */
public class LocalJsonFieldDeserializationExclusionStrategy implements ExclusionStrategy {
    /**
     * @param f the field object that is under test
     * @return true if the field should be ignored; otherwise false
     */
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        bas.lib.core.converter.gson.LocalJsonField anno =  f.getAnnotation(LocalJsonField.class);
        return anno != null && !anno.deserialize();
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
