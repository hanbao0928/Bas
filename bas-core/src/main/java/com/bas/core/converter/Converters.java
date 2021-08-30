package com.bas.core.converter;

import com.bas.core.converter.gson.GsonConverter;
import com.bas.core.converter.jackson.JacksonConverter;
import com.bas.core.lang.ClassUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by Lucio on 2021/7/21.
 */
public class Converters {

    private static JsonConverter mJsonConverter;

    private Converters() { }

    static {
        try {
            //初始化默认JsonConverter，优先使用Jackson
            if (ClassUtils.isClassExists("com.fasterxml.jackson.databind.ObjectMapper")) {
                mJsonConverter = new JacksonConverter();
            } else if (ClassUtils.isClassExists("com.google.gson.Gson")) {
                mJsonConverter = new GsonConverter();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setJsonConverter(@NotNull JsonConverter converter) {
        mJsonConverter = converter;
    }

    public static JsonConverter getJsonConverter() {
        return mJsonConverter;
    }

    @Nullable
    public static <T> T toObject(@Nullable String json, Class<T> clazz) {
        return mJsonConverter.toObject(json, clazz);
    }

    @Nullable
    public static <T> List<T> toObjectList(@Nullable String json, Class<T> clazz) {
        return mJsonConverter.toObjectList(json, clazz);
    }

    @Nullable
    public static String toJson(@Nullable Object obj) {
        return mJsonConverter.toJson(obj);
    }

}
