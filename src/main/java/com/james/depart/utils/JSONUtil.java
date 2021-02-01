package com.james.depart.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

/**
 * @author james
 * @description
 * @date 2020-12-30
 */
public class JSONUtil {

    public static ObjectMapper objectMapper = new ObjectMapper();

    //使用静态代码块 将objectMapper对象，将序列化对象设为默认方式
    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static JsonNode getJsonNode(String content) throws JsonProcessingException {
        return objectMapper.readTree(content);
    }

    public static <T> ObjectNode removeObjectNodeElement(JsonNode jsonNode, T value) {
        ObjectNode objectNode = (ObjectNode) jsonNode;
        objectNode.remove((String) value);
        return objectNode;
    }

    public static <T> ObjectNode removeObjectNodeElement(JsonNode jsonNode, T... value) {
        ObjectNode objectNode = (ObjectNode) jsonNode;
        for (int i = 0; i < value.length; i++) {
            objectNode.remove((String) value[i]);
        }

        for (T a : value) {
            objectNode.remove(a.toString());
        }

        return objectNode;
    }

    public static <T> ObjectNode removeObjectNodeElement2(JsonNode jsonNode, T... value) {
        ObjectNode objectNode = (ObjectNode) jsonNode;

        for (T a : value) {
            objectNode.remove(a.toString());
        }

        return objectNode;
    }

    static String removeJSONElement(String jsonData, String e) {
        JSONArray jsonArray = JSON.parseArray(jsonData);
        for (Object o : jsonArray) {
            JSONObject json = (JSONObject) o;
            json.remove(e);
        }
        return jsonArray.toString();
    }

    static String removeJson(String content, String e) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        for (JsonNode personNode : jsonNode) {
            if (personNode instanceof ObjectNode) {
                ObjectNode object = (ObjectNode) personNode;
                object.remove(e);
            }
        }
        return content;
    }

    /**
     * @param <T>
     * @param json  字符串
     * @param clazz class反射
     * @return
     * @字符串反序列化转对象
     */
    @SuppressWarnings({"finally", "unchecked"})
    public static <T> T Str2Obj(String json, Class<T> clazz) {
        T resp = null;
        if (json.isEmpty() || clazz == null) {
            return null;
        }
        try {
            resp = clazz.equals(String.class) ? (T) json : objectMapper.readValue(json, clazz);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            return resp;
        }
    }

    public static <T> T file2Obj(File file, Class<T> clazz) {
        T resp = null;
        if (!file.exists() || clazz == null) {
            return null;
        }
        try {
            resp = clazz.equals(String.class) ? (T) file : objectMapper.readValue(file, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }

    /**
     * @param <T>
     * @param objT
     * @return
     * @对象转json字符串
     */
    public static <T> String Obj2Str(T objT) {
        String resp = null;
        if (objT == null) {
            return null;
        }
        try {
            resp = (String) (objT.equals(String.class) ? objT : objectMapper.writeValueAsString(objT));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return resp;
    }

    /**
     * @param <T>
     * @param objT 传入对象
     * @return
     * @对象转格式化json字符串
     */
    public static <T> String Obj2StrPretty(T objT) {
        String resp = null;
        if (objT == null) {
            return null;
        }
        try {
            resp = (String) (objT.equals(String.class) ? objT : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objT));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return resp;
    }

    /**
     * @param <T>
     * @param json  字符串
     * @param clazz class反射
     * @return
     * @字符串反序列化转对象)(map，list对象)
     */
    @SuppressWarnings({"finally", "unchecked"})
    public static <T> T Str2Obj(String json, TypeReference<T> typeReference) {
        T resp = null;
        if (json.isEmpty() || typeReference == null) {
            return null;
        }
        try {
            resp = typeReference.getType().equals(String.class) ? (T) json : objectMapper.readValue(json, typeReference);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            return resp;
        }
    }

    /**
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @反序列化复杂类型字符串
     */
    public static <T> T Str2Obj(String json, Class<?> collectionClass, Class<?>... elementClass) throws JsonMappingException, JsonProcessingException {

        final JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClass);
        return objectMapper.readValue(json, javaType);
    }

}