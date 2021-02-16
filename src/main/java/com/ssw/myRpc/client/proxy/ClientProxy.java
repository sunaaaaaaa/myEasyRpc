package com.ssw.myRpc.client.proxy;

import java.lang.reflect.Proxy;
import java.util.Map;

//TODO 增添新的代理方式，增添用户配置，可由用户配置具体的代理方式
public class ClientProxy {

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<?> className, Map<String, Object> attr) {
        return (T) Proxy.newProxyInstance(className.getClassLoader(),
                new Class<?>[]{className},
                new ProxyHandler(attr));
    }
}
