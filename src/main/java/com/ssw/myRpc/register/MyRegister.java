package com.ssw.myRpc.register;

import com.ssw.myRpc.bean.ServiceInfo;
import com.ssw.myRpc.bean.ServiceInfos;

import java.util.List;
import java.util.Map;

public interface MyRegister {

    //注册服务
    void register(String serviceName, ServiceInfo list) throws Exception;

    //注销服务
    void unRegister(String serviceName);

    //发现服务
    List<ServiceInfo> findService(String serviceName);

    //获取注册中心注册的所有服务
    Map<String, ServiceInfos> findAllServices() throws Exception;

    //监听
    void watch(CallBackEvent callBackEvent);

    //心跳
    void keepAlive();
}
