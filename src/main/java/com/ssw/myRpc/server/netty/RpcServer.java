package com.ssw.myRpc.server.netty;

import com.ssw.myRpc.bean.Constant;
import com.ssw.myRpc.bean.RpcRequest;
import com.ssw.myRpc.bean.RpcResponse;
import com.ssw.myRpc.codec.RpcDecoder;
import com.ssw.myRpc.codec.RpcEncoder;
import com.ssw.myRpc.config.StarterAutoConfig;
import com.ssw.myRpc.config.StarterServerProperties;
import com.ssw.myRpc.serializer.MySerializer;
import com.ssw.myRpc.serializer.SerializerEngine;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class RpcServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    private StarterAutoConfig autoConfig = Constant.properties;
    private MySerializer serializer = SerializerEngine.getSerializer(autoConfig.getProperties().getSerializerType());

    //启动服务器
    public void run(Map<String, Object> handlerMap) {
        EventLoopGroup boosGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(boosGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                //.option(ChannelOption.SO_BACKLOG,128)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
                        pipeline.addLast(new RpcEncoder(RpcResponse.class, serializer));
                        pipeline.addLast(new RpcDecoder(RpcRequest.class, serializer));
                        pipeline.addLast(new RpcServerHandler(handlerMap));
                    }
                }).childOption(ChannelOption.SO_KEEPALIVE, true);//检测客户端是否还存活;

        try {
            StarterServerProperties serverProperties = autoConfig.getServerProperties();
            ChannelFuture sync = bootstrap.bind(serverProperties.getServicePort()).sync();
            //采用异步监听关闭事件
            sync.channel().closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    boosGroup.shutdownGracefully();
                    workGroup.shutdownGracefully();
                    LOGGER.info("close rpc server");
                }
            });
            //sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error("service server starter failure!");
            throw new RuntimeException(e);
        }


    }
}
