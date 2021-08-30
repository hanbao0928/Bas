package com.bas.core.converter.fastjson;

import com.alibaba.fastjson.JSON;
import com.bas.core.converter.JsonConverter;
import com.bas.core.lang.StringUtils;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by Lucio on 2021/7/21.
 */
public class FastJsonConverter implements JsonConverter {

    @Nullable
    @Override
    public <T> T toObject(@Nullable String json, Class<T> clazz) {
        if (StringUtils.isNullOrEmpty(json))
            return null;
        return JSON.parseObject(json, clazz);
    }

    @Nullable
    @Override
    public <T> List<T> toObjectList(@Nullable String json, Class<T> clazz) {
        if (StringUtils.isNullOrEmpty(json))
            return null;
        return JSON.parseArray(json, clazz);
    }

    @Nullable
    @Override
    public String toJson(@Nullable Object obj)  {
        if (obj == null)
            return null;
        return JSON.toJSONString(obj);
    }

}
