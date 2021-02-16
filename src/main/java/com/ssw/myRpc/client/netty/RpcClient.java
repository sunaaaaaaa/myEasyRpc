package com.ssw.myRpc.client.netty;

import com.ssw.myRpc.bean.*;
import com.ssw.myRpc.codec.RpcDecoder;
import com.ssw.myRpc.codec.RpcEncoder;
import com.ssw.myRpc.config.StarterClientProperties;
import com.ssw.myRpc.config.StarterProperties;
import com.ssw.myRpc.register.MyRegister;
import com.ssw.myRpc.register.RegisterHelper;
import com.ssw.myRpc.serializer.MySerializer;
import com.ssw.myRpc.serializer.SerializerEngine;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


//负责和服务提供者通信
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);

    private static Map<String, ServiceInfos> serviceMap;

    private static MyRegister register;
    private static StarterProperties starterProperties;
    private static StarterClientProperties clientProperties;

    private MySerializer serializer = SerializerEngine.getSerializer(starterProperties.getSerializerType());
    private RpcResponse response;

    public static void init() throws Exception {
        starterProperties = Constant.properties.getProperties();
        clientProperties = Constant.properties.getClientProperties();
        register = RegisterHelper.getRegister(starterProperties.getRegister());
        serviceMap = register.findAllServices();
    }

    public RpcResponse run(RpcRequest request,String serviceIp,int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建Netty客户端进行通信
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new RpcEncoder(RpcRequest.class, serializer));
                            pipeline.addLast(new RpcDecoder(RpcResponse.class, serializer));
                            pipeline.addLast(RpcClient.this);
                        }
                    });

            ChannelFuture sync = bootstrap.connect(serviceIp, port).sync();
            Channel channel = sync.channel();
            channel.writeAndFlush(request).sync();
            channel.closeFuture().sync();
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            group.shutdownGracefully();
        }
    }

    public static Map<String, ServiceInfos> getServiceMap() {
        return serviceMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse response) throws Exception {
        this.response = response;
        System.out.println(response.getResult());
    }
}
