package com.ssw.myRpc.serializer;


import java.util.HashMap;
import java.util.Map;


public class SerializerEngine {


    private static Map<String, MySerializer> serializerMap = new HashMap<>();

    static {
        serializerMap.put("default", new DefaultSerializer());
        serializerMap.put("json", new JSONSerializer());
    }


    public static MySerializer getSerializer(String serializerType) {
        if (serializerMap.containsKey(serializerType)) {
            return serializerMap.get(serializerType);
        } else {
            throw new RuntimeException("当前不支持该序列化方式");
        }
    }
}
