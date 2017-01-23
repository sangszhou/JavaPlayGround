package util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

/**
 * Created by xinszhou on 25/12/2016.
 */
public class JsonUtil {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        // non-ascii characters will be escaped in json string
        mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        // null value will not be included in json string
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // unknown properties will be ignored during deserialization
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static Optional<JsonNode> getNodeByPath(String path, JsonNode root) {
        JsonNode current = root;
        String[] list = path.split("\\.");
        for (String name : list) {
            if (current.isObject() && current.has(name))
                current = current.get(name);
            else return Optional.empty();
        }
        return Optional.of(current);
    }

    public static String toString(Object obj) throws IOException {
        return mapper.writeValueAsString(obj);
    }

    public static <T> T fromJson(String jsonString, Class<T> c) throws IOException {
        return mapper.readValue(jsonString, c);
    }

    public static <T> T convert(JsonNode fromJson, Class<T> tClass) throws IllegalArgumentException {
        return mapper.convertValue(fromJson, tClass);
    }

    public static JsonNode string2Json(String input) throws IOException {
        return mapper.readTree(input);
    }

    public static JsonNode inputStream2Json(InputStream inputStream) throws IOException {
        return mapper.readTree(inputStream);
    }

    public static void json2outputStream(Object obj, OutputStream outputStream) throws IOException {
        mapper.writeValue(outputStream, obj);
    }

}
