package com.bas.core.converter.gson;


import com.bas.core.converter.JsonConverter;
import com.bas.core.lang.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by Lucio on 2021/7/21.
 */
public class GsonConverter implements JsonConverter {

    private final Gson mGson;

    /**
     * 是否内联优化：即如果是kotlin场景，在解析的时候直接使用{@link #mGson}进行解析，而不是用to之类的方法
     */
    private boolean isInlineOptimization = true;

    public GsonConverter() {
        this(newGsonBuilder().create());
    }

    public GsonConverter(Gson gson) {
        this.mGson = gson;
    }

    public Gson getGson() {
        return mGson;
    }

    public boolean isInlineOptimization() {
        return isInlineOptimization;
    }

    public void setInlineOptimization(boolean inlineOptimization) {
        isInlineOptimization = inlineOptimization;
    }

    @Nullable
    @Override
    public <T> T toObject(@Nullable String json, Class<T> clazz) {
        if (StringUtils.isNullOrEmpty(json))
            return null;
        return this.mGson.fromJson(json, clazz);
    }

    @Nullable
    @Override
    public <T> List<T> toObjectList(@Nullable String json, Class<T> clazz) {
        ParameterizedTypeImpl type = new ParameterizedTypeImpl(List.class, new Class<?>[]{clazz}, null);
        return this.mGson.fromJson(json, type);
        //        /*无法使用下面代码完成反序列化，如果是直接使用而不是封装在方法中是可以的
//        * 具体参考：https://www.jianshu.com/p/d62c2be60617*/
////        if (json.isNullOrEmpty())
////            return null
////        val type: Type = object : TypeToken<List<T>>() {}.type
////        return gson.fromJson(json, type)

//        该方法也可以，但是效率不高
        ////        //Gson不能解析泛型类型，只能用下面方法折中处理：gson可以支持内联的泛型方法
////        if (json.isNullOrEmpty())
////            return null
////        //这种方法则：转换成jsonArray之后再将每一个element转换成对应的object class
////        val results = mutableListOf<T>()
////
////        val arry = JsonParser().parse(json).asJsonArray
////        arry.forEach {
////            results.add(gson.fromJson(it, clazz))
////        }
////        return results
    }

    @Nullable
    @Override
    public String toJson(@Nullable Object obj) {
        if (obj == null)
            return null;
        return this.mGson.toJson(obj);
    }

    public static GsonBuilder newGsonBuilder() {
        return new GsonBuilder();
    }

    public static GsonBuilder newStrategyGsonBuilder() {
        return newGsonBuilder().addDeserializationExclusionStrategy(new LocalJsonFieldDeserializationExclusionStrategy())
                .addSerializationExclusionStrategy(new LocalJsonFieldSerializationExclusionStrategy());
    }
}
