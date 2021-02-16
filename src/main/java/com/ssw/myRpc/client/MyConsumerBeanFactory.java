package com.ssw.myRpc.client;

import com.ssw.myRpc.client.proxy.ClientProxy;
import org.springframework.beans.factory.FactoryBean;

import java.util.Map;

//用于保存被RpcConsumer接口标注的接口的相关信息，因为Spring无法为接口生成代理，只能通过ConsumerBeanDefinitionConfig
//将对应的接口扫描出来，然后对将其中包含的信息封装为该类的实例，并注册到容器中，以供后面使用
public class MyConsumerBeanFactory<T> implements FactoryBean<T> {

    private Class<?> targetClass;
    private Map<String, Object> attr;

    //此处形成产生接口代理的BeanFactory，代理的功能就是根据serviceName
    public MyConsumerBeanFactory(Class<?> targetClass, Map<String, Object> attr) {
        this.targetClass = targetClass;
        this.attr = attr;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public T getObject() throws Exception {
        return ClientProxy.create(targetClass, attr);
    }

    @Override
    public Class<?> getObjectType() {
        return targetClass;
    }
}
