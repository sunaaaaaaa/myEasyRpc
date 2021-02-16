package com.ssw.myRpc.serializer;

public enum SerializeType {

    DefaultSerializer("DefaultSerializer"),
    JSONSerializer("JSONSerializer"),
    ProtosBufSerializer("ProtosBufSerializer");

    private String SerializeName;

    private SerializeType(String serializeName) {
        this.SerializeName = serializeName;
    }

    public String getSerializeName() {
        return SerializeName;
    }
}
