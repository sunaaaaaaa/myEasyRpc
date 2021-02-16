package com.ssw.myRpc.annotation;


import com.ssw.myRpc.client.ConsumerBeanDefinitionConfig;
import com.ssw.myRpc.client.RpcClientBootstrap;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//服务消费者者启动入口类
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcClientBootstrap.class, ConsumerBeanDefinitionConfig.class})  //引入整个框架的初始化启动类
public @interface EnableMyRpcClient {
    String scanPath();
}
