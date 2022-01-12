package com.bas.core.converter;


import org.jetbrains.annotations.Nullable;

import java.io.IOException;
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
    <T> String toJson(@Nullable T obj) ;

}
