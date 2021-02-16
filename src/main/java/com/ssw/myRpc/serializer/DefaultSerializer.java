package com.ssw.myRpc.serializer;


//TODO  实现序列化
public class DefaultSerializer implements MySerializer {
    @Override
    public <T> byte[] encoder(T obj) {
        return new byte[0];
    }

    @Override
    public <T> T decoder(byte[] data, Class<T> clazz) {
        return null;
    }

    @Override
    public Object decoderToObject(byte[] data) {
        return null;
    }
}
