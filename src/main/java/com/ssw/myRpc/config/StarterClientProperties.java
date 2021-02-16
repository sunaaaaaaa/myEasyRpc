package com.ssw.myRpc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

//客户端方面的配置,配置重试、超时时间、负载均衡策略等
@ConfigurationProperties("ssw.rpc.client")
public class StarterClientProperties {
    //private String loadBalanceType;
    private int retryNums;//重试次数
    private int retryTime;//超时时间

//    public String getLoadBalanceType() {
//        return loadBalanceType;
//    }
//
//    public void setLoadBalanceType(String loadBalanceType) {
//        this.loadBalanceType = loadBalanceType;
//    }

    public int getRetryNums() {
        return retryNums;
    }

    public void setRetryNums(int retryNums) {
        this.retryNums = retryNums;
    }

    public int getRetryTime() {
        return retryTime;
    }

    public void setRetryTime(int retryTime) {
        this.retryTime = retryTime;
    }
}
