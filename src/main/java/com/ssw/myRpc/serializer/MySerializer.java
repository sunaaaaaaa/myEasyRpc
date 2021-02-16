package com.ssw.myRpc.serializer;

public interface MySerializer {
    <T> byte[] encoder(T obj);

    <T> T decoder(byte[] data,Class<T> clazz);

    Object decoderToObject(byte[] data);
}
