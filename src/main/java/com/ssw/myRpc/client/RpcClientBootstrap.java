package com.ssw.myRpc.client;


import com.ssw.myRpc.bean.Constant;
import com.ssw.myRpc.client.netty.RpcClient;
import com.ssw.myRpc.config.StarterAutoConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


//负责注册中心的信息获取部分，初始化RpcClient，连接注册中心，获取注册中心提供者的信息，并将其保存，提供给用户使用
public class RpcClientBootstrap implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClientBootstrap.class);


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Constant.properties = applicationContext.getBean("ssw-rpc-AutoConfig", StarterAutoConfig.class);
    }

    //初始化客户端，连接注册中心，并获取提供者的信息
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //获取注册中心相关属性，提供给RpcClient，并进行RpcClient的初始化
        try {
            RpcClient.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
