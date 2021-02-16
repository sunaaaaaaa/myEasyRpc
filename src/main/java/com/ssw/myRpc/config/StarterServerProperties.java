package com.ssw.myRpc.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

//服务端方面的配置，配置限流策略、熔断策略等等
@ConfigurationProperties("ssw.rpc.server")
public class StarterServerProperties {

    private String ServiceIp;
    private int ServicePort;

    public String getServiceIp() {
        return ServiceIp;
    }

    public void setServiceIp(String serviceIp) {
        ServiceIp = serviceIp;
    }

    public int getServicePort() {
        return ServicePort;
    }

    public void setServicePort(int servicePort) {
        ServicePort = servicePort;
    }
}
