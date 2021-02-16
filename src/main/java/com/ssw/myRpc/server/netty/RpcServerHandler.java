package com.ssw.myRpc.server.netty;

import com.ssw.myRpc.bean.RpcRequest;
import com.ssw.myRpc.bean.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerHandler.class);
    //key:服务的全类名    value:具体的处理类
    private final Map<String, Object> handlerMap;

    RpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    //远程调用的处理部分
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        RpcResponse response = new RpcResponse();
        response.setRequestId(rpcRequest.getRequestId());

        //执行rpc
        String serviceName = rpcRequest.getServiceName();
        String methodName = rpcRequest.getMethodName();
        Class<?>[] paramsType = rpcRequest.getParamsType();
        Object[] params = rpcRequest.getParams();
        Object handler = handlerMap.get(serviceName);
        System.out.println(handler.toString().split(" ")[1]);
        //Object handler = serviceInfos;
        Class<?> handlerClass = Class.forName(handler.toString().split(" ")[1]);
        System.out.println(handlerClass);
        Method method = handlerClass.getDeclaredMethod(methodName, paramsType);
        if (method == null) {
            LOGGER.error("cannot find this method!");
            response.setMessage("cannot find this method!");
        } else {
            method.setAccessible(true);
            Object result = method.invoke(handlerClass.newInstance(), params);
            response.setMessage("exec method success!");
            response.setResult(result);
        }

        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
