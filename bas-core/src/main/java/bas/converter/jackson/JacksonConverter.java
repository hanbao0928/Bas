package bas.converter.jackson;

import bas.converter.JsonConverter;
import bas.lang.DateUtils;
import bas.lang.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucio on 2021/7/21.
 */
public class JacksonConverter implements JsonConverter {

    private final ObjectMapper objectMapper;

    public JacksonConverter() {
        this(createDefaultObjectMapper());
    }

    public JacksonConverter(@NotNull ObjectMapper mapper) {
        this.objectMapper = mapper;
    }

    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    @Nullable
    @Override
    public <T> T toObject(@Nullable String json, Class<T> clazz) {
        if (StringUtils.isNullOrEmpty(json))
            return null;
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new DeserializeException(e);
        }
    }

    @Nullable
    @Override
    public <T> List<T> toObjectList(@Nullable String json, Class<T> clazz) {
        if (StringUtils.isNullOrEmpty(json))
            return null;
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
        try {
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            throw new DeserializeException(e);
        }
    }

    @Nullable
    @Override
    public String toJson(@Nullable Object obj) {
        if (obj == null)
            return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new SerializeException(e);
        }
    }

    public static ObjectMapper createDefaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        //不存在属性字段时不发生错误
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    /**
     * 使用UTC时间格式
     * @param om
     */
    public static ObjectMapper applyUTCDateFormat(ObjectMapper om){
        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        om.setDateFormat(DateUtils.getUTCDateTimeFormat());
        return om;
    }
}
