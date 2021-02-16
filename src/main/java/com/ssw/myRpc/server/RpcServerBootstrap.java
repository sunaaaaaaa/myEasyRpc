package com.ssw.myRpc.server;


import com.ssw.myRpc.annotation.ExposeMethod;
import com.ssw.myRpc.annotation.RpcProvider;
import com.ssw.myRpc.bean.Constant;
import com.ssw.myRpc.bean.MethodInfo;
import com.ssw.myRpc.bean.ServiceInfo;
import com.ssw.myRpc.config.StarterAutoConfig;
import com.ssw.myRpc.register.MyRegister;
import com.ssw.myRpc.register.RegisterHelper;
import com.ssw.myRpc.server.netty.RpcServer;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

//服务提供者入口类，进行一些初始化操作，启动RpcServer
public class RpcServerBootstrap implements ApplicationContextAware, BeanPostProcessor, ApplicationListener<ContextRefreshedEvent> {


    private Map<String, Object> handlersMap = new HashMap<>();//存储暴露的服务的处理类
    //存储要暴露的服务的抽象，将这些信息发送到注册中心,K为服务名，v为该服务的所有服务方法
    private Map<String, ServiceInfo> infosMap = new HashMap<>();

    //扫描所有的Bean，对标注提供者注解的类进行处理，放到handlerMap中，并进行注册
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //查看该类是否含有@Provider注解
        RpcProvider provider = AnnotationUtils.findAnnotation(bean.getClass(), RpcProvider.class);


        if (provider == null) return bean;
        //如果标注了该注解，继续下面的处理
        //获取容器中该类的最终代理Class，即aop加强过的
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());

        String serviceName = provider.serviceName();
        System.out.println("find an provider:" + serviceName);
        if (methods == null) return bean;

        //将Service封装为ServiceInfo
        ServiceInfo info = new ServiceInfo();
        info.setServiceName(serviceName);
        info.setServiceIp(Constant.properties.getServerProperties().getServiceIp());
        info.setServicePort(Constant.properties.getServerProperties().getServicePort());
        info.setSerializeType(Constant.properties.getProperties().getSerializerType());
        infosMap.putIfAbsent(serviceName, info);
        //将整个服务类放到一个Map中，根据消费者提供的名字找到该类，再根据方法名反射调用
        handlersMap.putIfAbsent(serviceName, targetClass);
        for (Method method : methods) {
            //找出所有要暴露的服务方法
            ExposeMethod exposeMethod = AnnotationUtils.findAnnotation(method, ExposeMethod.class);
            if (null == exposeMethod || method.getDeclaredAnnotations().length == 0) continue;

            //将暴露的方法封装为MethodInfo对象，存到ServiceInfo
            MethodInfo methodInfo = new MethodInfo();
            methodInfo.setMethodName(method.getName());
            methodInfo.setBio(exposeMethod.methodIntro());
            info.getMethods().add(methodInfo);
        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Constant.properties = applicationContext.getBean("ssw-rpc-AutoConfig", StarterAutoConfig.class);
    }

    //spring初始化完毕后启动整个框架
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
            init(applicationContext);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void init(ApplicationContext applicationContext) throws Exception {
        //1.初始化注册中心
        StarterAutoConfig config = applicationContext.getBean("ssw-rpc-AutoConfig", StarterAutoConfig.class);
        // 1.1 根据用户的配置确定注册中心的类型
        MyRegister register = RegisterHelper.getRegister(config.getProperties().getRegister());
        System.out.println("register type: "+config.getProperties().getRegister() + ":" + register);
        //2.暴露服务
        for (Map.Entry<String, ServiceInfo> entry : infosMap.entrySet()) {
            register.register(entry.getKey(), entry.getValue());
        }

        //TODO 3.设置监听
        //4.启动Server服务器
        RpcServer server = new RpcServer();
        //这里要异步，否则会阻塞当前线程，使得spring容器初始化过程阻塞
        server.run(handlersMap);
    }
}
