package com.bas.core.converter;


import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by Lucio on 2021/7/21.
 */
public interface JsonConverter {

    @Nullable
    <T> T toObject(@Nullable String json, Class<T> clazz) ;

    @Nullable
    <T> List<T> toObjectList(@Nullable String json, Class<T> clazz);

    @Nullable
    String toJson(@Nullable Object obj) ;

}
