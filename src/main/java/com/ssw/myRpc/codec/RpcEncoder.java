package com.ssw.myRpc.codec;

import com.ssw.myRpc.serializer.MySerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> clazz;
    private MySerializer serializer;

    public RpcEncoder(Class<?> clazz, MySerializer serializer) {
        this.clazz = clazz;
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        //如果o是RpcRequest类型，则编码
        if (clazz.isInstance(o)) {
            byte[] data = serializer.encoder(o);
            byteBuf.writeInt(data.length);
            byteBuf.writeBytes(data);
        }
    }
}
