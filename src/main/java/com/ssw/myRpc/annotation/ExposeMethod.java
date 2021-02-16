package com.ssw.myRpc.annotation;



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//provider要暴露的方法
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface ExposeMethod {
    //方法简介,描述该服务具体是做什么的
    String methodIntro() default "nothing method info";
}
