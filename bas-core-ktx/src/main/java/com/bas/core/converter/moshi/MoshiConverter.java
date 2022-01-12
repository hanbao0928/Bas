package com.bas.core.converter.moshi;

import com.bas.core.converter.JsonConverter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Lucio on 2022/1/12.
 */
public class MoshiConverter implements JsonConverter {

    private final Moshi moshi;

    public MoshiConverter() {
        this(createPreferredMoshi());
    }

    public MoshiConverter(Moshi moshi) {
        this.moshi = moshi;
    }

    @Nullable
    @Override
    public <T> T toObject(@Nullable String json, Class<T> clazz)  {
        JsonAdapter<T> jsonAdapter = moshi.adapter(clazz);
        try {
            return jsonAdapter.fromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public @Nullable <T> List<T> toObjectList(@Nullable String json, Class<T> clazz)  {
        Type type = Types.newParameterizedType(List.class, clazz);
        JsonAdapter<List<T>> adapter = moshi.adapter(type);
        try {
            return adapter.fromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public @Nullable <T> String toJson(@Nullable T obj) {
        Type type = Types.getRawType(obj.getClass());
        return moshi.adapter(type).toJson(obj);
    }

    public @Nullable <T> String toJson(@Nullable T obj,Class<T> clazz) {
        return moshi.adapter(clazz).toJson(obj);
    }

//    @Override
//    public @Nullable String toJson(@Nullable Object obj) {
//        return moshi.adapter(obj.getClass()).toJson(obj);
//    }

    public static Moshi createPreferredMoshi() {
        return new Moshi.Builder().build();
    }

    public static Moshi.Builder applyUtcDate(Moshi.Builder builder){
        builder.add(new UtcDateAdapter());
        return builder;
    }
}
