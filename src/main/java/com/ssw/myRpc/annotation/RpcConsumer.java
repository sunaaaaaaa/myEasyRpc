package com.ssw.myRpc.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//消费者注解，类似于feign注解
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcConsumer {
    String serviceName();//要消费的服务名,必填

    //路由策略
    String loadBalance();//默认路由策略使用随机策略
}
