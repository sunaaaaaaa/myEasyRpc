package com.ssw.myRpc.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//提供者注解,标志该类为一个Provider，然后扫描其中要暴露的方法
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcProvider {
    //服务名称
    String serviceName();
}
