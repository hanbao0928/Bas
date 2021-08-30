package com.bas.core.converter

import com.bas.core.converter.gson.GsonConverter
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Created by Lucio on 2021/7/22.
 */

inline fun Any?.toJson(): String? {
    return Converters.toJson(this)
}

inline fun <reified T> String?.toObject(): T? {
    return Converters.toObject(this, T::class.java)
}

inline fun <reified T> String?.toObjectList(): List<T>? {
    if (this.isNullOrEmpty())
        return null
    val convert = Converters.getJsonConverter()
    return if (convert is GsonConverter && convert.isInlineOptimization) {
        //gson不支持泛型方法，所以必须在内联方法中实现；
        val type: Type = object : TypeToken<List<T>>() {}.type
        convert.gson.fromJson(this, type)
    } else {
        convert.toObjectList(this, T::class.java)
    }
}