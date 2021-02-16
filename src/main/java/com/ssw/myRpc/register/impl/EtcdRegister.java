package com.ssw.myRpc.register.impl;

import com.ssw.myRpc.bean.ServiceInfo;
import com.ssw.myRpc.bean.ServiceInfos;
import com.ssw.myRpc.register.CallBackEvent;
import com.ssw.myRpc.register.MyRegister;

import java.util.List;
import java.util.Map;

public class EtcdRegister implements MyRegister {


    @Override
    public void register(String serviceName, ServiceInfo list) {

    }

    @Override
    public void unRegister(String serviceName) {

    }

    @Override
    public List<ServiceInfo> findService(String serviceName) {
        return null;
    }

    @Override
    public Map<String, ServiceInfos> findAllServices() {
        return null;
    }

    @Override
    public void watch(CallBackEvent callBackEvent) {

    }

    @Override
    public void keepAlive() {

    }
}
