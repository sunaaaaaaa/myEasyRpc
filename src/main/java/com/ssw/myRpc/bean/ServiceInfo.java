package com.ssw.myRpc.bean;

import java.util.ArrayList;
import java.util.List;

//提供者提供的服务抽象，即注册中心中保存的服务提供者相关的信息
public class ServiceInfo {

    private String serviceName;
    private String serviceIp;
    private int servicePort;
    private String serializeType;
    private List<MethodInfo> methods = new ArrayList<>();

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }


    public String getSerializeType() {
        return serializeType;
    }

    public void setSerializeType(String serializeType) {
        this.serializeType = serializeType;
    }


    public String getServiceIp() {
        return serviceIp;
    }

    public void setServiceIp(String serviceIp) {
        this.serviceIp = serviceIp;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodInfo> methods) {
        this.methods = methods;
    }
}
