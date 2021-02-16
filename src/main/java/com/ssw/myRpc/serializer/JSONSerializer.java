package com.ssw.myRpc.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;


public class JSONSerializer implements MySerializer {
    @Override
    public <T> byte[] encoder(T obj) {

        return JSON.toJSONString(obj).getBytes();
    }

    @Override
    public <T> T decoder(byte[] data,Class<T> clazz) {

        return JSONObject.parseObject(data,clazz);
    }

    @Override
    public Object decoderToObject(byte[] data) {

        return JSONObject.parseObject(Arrays.toString(data));
    }
}
