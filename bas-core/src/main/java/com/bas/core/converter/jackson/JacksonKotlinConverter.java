package com.bas.core.converter.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Lucio on 2022/1/12.
 */
public class JacksonKotlinConverter extends JacksonConverter {

    public JacksonKotlinConverter() {
        this(createDefaultKotlinObjectMapper());
    }

    public JacksonKotlinConverter(@NotNull KotlinModule module) {
        this(new ObjectMapper().registerModule(module));
    }

    /**
     * @param mapper 记得传递进来的对象配置KotlinModule
     */
    public JacksonKotlinConverter(@NotNull ObjectMapper mapper) {
        super(mapper);
    }

    public static ObjectMapper createDefaultKotlinObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper = com.fasterxml.jackson.module.kotlin.ExtensionsKt.registerKotlinModule(mapper);
        //不存在属性字段时不发生错误
        mapper = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

}
