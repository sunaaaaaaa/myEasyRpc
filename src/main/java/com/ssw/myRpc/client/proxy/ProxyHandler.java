package com.ssw.myRpc.client.proxy;


import com.ssw.myRpc.bean.*;
import com.ssw.myRpc.client.netty.RpcClient;
import com.ssw.myRpc.loadbalance.LoadBalanceHelper;
import com.ssw.myRpc.loadbalance.MyLoadBalance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class ProxyHandler implements InvocationHandler {

    private Map<String, Object> attr;
    private MyLoadBalance balance;
    private String serviceName;

    public ProxyHandler(Map<String, Object> attr) {
        this.attr = attr;
        String balanceType = attr.get("loadBalance").toString();
        balance = LoadBalanceHelper.getLoadBalanceMap().get(balanceType);
        serviceName = attr.get("serviceName").toString();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String methodName = method.getName();
        if(!RpcClient.getServiceMap().containsKey(serviceName)){
            throw new RuntimeException("can't find service" + serviceName);
        }
        ServiceInfos serviceInfos = RpcClient.getServiceMap().get(serviceName);

        //负载均衡
        int select = balance.select(serviceInfos.getServiceInfos().size());
        //取出处理该次请求的实例
        ServiceInfo serviceInfo = serviceInfos.getServiceInfos().get(select);
        RpcResponse response = null;
        //取出提供者提供的该服务所暴露的所有方法
        for (MethodInfo serviceInfoMethod : serviceInfo.getMethods()) {
            //如果要调用的方法和Provider提供的服务方法匹配，则远程调用，否则抛出异常
            if (methodName.equals(serviceInfoMethod.getMethodName())) {
                RpcClient client = new RpcClient();
                RpcRequest request = new RpcRequest();
                request.setServiceName(serviceName);
                request.setMethodName(methodName);
                request.setRequestId(UUID.randomUUID().toString());
                request.setParams(args);
                request.setParamsType(method.getParameterTypes());
                response = client.run(request,serviceInfo.getServiceIp(),serviceInfo.getServicePort());
                if(response == null){
                    throw new RuntimeException("rpc failed");
                }
            }
        }
        if(response==null){
            throw new RuntimeException("this method not expose" + methodName);
        }

        return response.getResult();
    }
}
