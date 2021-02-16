package com.ssw.test.demo;

import com.ssw.myRpc.annotation.RpcConsumer;

@RpcConsumer(serviceName = "helloService",loadBalance = "default")
public interface HelloService {

    String hello(String a);
    String hello2(String a);
    String hello3(String a);
}
