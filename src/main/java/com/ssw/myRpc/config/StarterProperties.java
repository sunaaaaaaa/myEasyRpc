package com.ssw.myRpc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

//用户可以通过properties文件配置的通用属性
@ConfigurationProperties("ssw.rpc.properties")
public class StarterProperties {

    //通用配置
    //注册中心的类型
    private String register;
    //注册中心ip地址
    private String registerIp;
    //注册中心端口
    private String registerPort;
    //序列化方式
    private String serializerType;

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public String getRegisterPort() {
        return registerPort;
    }

    public void setRegisterPort(String registerPort) {
        this.registerPort = registerPort;
    }

    public String getSerializerType() {
        return serializerType;
    }

    public void setSerializerType(String serializerType) {
        this.serializerType = serializerType;
    }
}
