package com.ssw.test.server;

import com.ssw.myRpc.annotation.ExposeMethod;
import com.ssw.myRpc.annotation.RpcProvider;
import org.springframework.stereotype.Service;


@Service
@RpcProvider(serviceName = "helloService")
public class HelloService {

    @ExposeMethod
    public String hello(String a){
        return "server hello test " + a;
    }

    public String hello2(String a){
        return "server hello2 test " + a;
    }

    @ExposeMethod
    public String hello3(String a){
        return "server hello3 test " + a;
    }
}
