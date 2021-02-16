package com.ssw.myRpc.annotation;

import com.ssw.myRpc.server.RpcServerBootstrap;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//服务提供者启动入口类
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcServerBootstrap.class})  //引入整个框架的初始化启动类
//@ComponentScan("com.ssw.myRpc.*")
public @interface EnableMyRpcServer {
}
