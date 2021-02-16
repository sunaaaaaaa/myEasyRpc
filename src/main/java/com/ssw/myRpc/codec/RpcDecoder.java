package com.ssw.myRpc.codec;

import com.ssw.myRpc.serializer.MySerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> clazz;
    private MySerializer serializer;

    public RpcDecoder(Class<?> clazz, MySerializer serializer) {
        this.clazz = clazz;
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //前4个字节为长度
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();//读取数据长度
        //未读取完继续等待
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }
        //读取完毕
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);
        Object o = serializer.decoder(data,clazz);
        list.add(o);
    }
}
