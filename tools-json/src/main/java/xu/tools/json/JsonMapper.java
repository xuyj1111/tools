package xu.tools.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonMapper {
    @Getter
    private static ObjectMapper mapper = new ObjectMapper();

    private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);

    /**
     * @Description: 配置ObjectMapper
     */
    static {
        //float序列化为BigDecimal功能开启，默认序列化为Double
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        //int序列化为BigInteger功能开启，默认序列化为值最小类型
        mapper.enable(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS);
        //过长时会转换为科学计数法，此设置则不显示科学计数法
        mapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        // 转换为格式化的json 显示出来的格式美化
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        //JsonInclude.Include.NON_DEFAULT 属性为默认值不序列化
        //JsonInclude.Include.ALWAYS      所有属性
        //JsonInclude.Include.NON_EMPTY   属性为 空（“”） 或者为 NULL 都不序列化
        //JsonInclude.Include.NON_NULL    属性为NULL 不序列化
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //反序列化时,遇到未知属性会不会报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //如果是空对象的时候,不抛异常
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 忽略 transient 修饰的属性
        mapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);
        //修改序列化后日期格式
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //该特性决定parser将是否允许解析使用Java/C++ 样式的注释（包括'/'+'*' 和'//' 变量）
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //处理不同的时区偏移格式
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
    }

    /**
     * @Description: 将object转换为json
     */
    public static String writeValueAsString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.warn("write value as string error:" + object, e);
            return null;
        }
    }

    /**
     * @Description: 将json转换为指定类型的对象
     */
    public static <T> T readValue(String jsonStr, Class<T> clazz) {
        if (Strings.isBlank(jsonStr)) {
            return null;
        }
        try {
            return mapper.readValue(jsonStr, clazz);
        } catch (IOException e) {
            logger.warn("read value error:" + jsonStr, e);
            return null;
        }
    }

    /**
     * @Description: 将json转换为指定JavaType类型的对象
     */
    public static <T> T readValue(String jsonStr, JavaType javaType) {
        if (Strings.isBlank(jsonStr)) {
            return null;
        }
        try {
            return mapper.readValue(jsonStr, javaType);
        } catch (IOException e) {
            logger.warn("read value error:" + jsonStr, e);
            return null;
        }
    }

    /**
     * @Description: 将json转换为JsonNode对象
     */
    public static JsonNode readTree(String jsonStr) {
        if (Strings.isBlank(jsonStr)) {
            return null;
        }
        try {
            return mapper.readTree(jsonStr);
        } catch (IOException e) {
            logger.warn("read tree error:" + jsonStr, e);
            return null;
        }
    }

    /**
     * @Description: 将json转换为范型为指定类型的Set
     */
    public static <T> Set<T> parseSet(String jsonStr, Class<T> elementClass) {
        return readValue(jsonStr, buildCollectionType(HashSet.class, elementClass));
    }

    /**
     * @Description: 将json转换为范型为指定类型的List
     */
    public static <T> List<T> parseList(String jsonStr, Class<T> elementClass) {
        return readValue(jsonStr, buildCollectionType(ArrayList.class, elementClass));
    }

    /**
     * @Description: 将json转换为范型为指定类型的Map
     */
    public static <T, R> Map<T, R> parseMap(String jsonStr, Class<T> keyClass, Class<R> valueClass) {
        return readValue(jsonStr, buildMapType(HashMap.class, keyClass, valueClass));
    }

    /**
     * @Description: 构建指定范型的Collection的JavaType对象
     */
    public static JavaType buildCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
        return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

    /**
     * @Description: 构建指定范型的Map的JavaType对象
     */
    public static JavaType buildMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
        return mapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
    }

}