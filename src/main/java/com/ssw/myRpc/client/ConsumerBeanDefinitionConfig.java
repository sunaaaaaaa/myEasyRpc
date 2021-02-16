package com.ssw.myRpc.client;


import com.ssw.myRpc.annotation.EnableMyRpcClient;
import com.ssw.myRpc.annotation.RpcConsumer;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Set;

//处理所有消费者接口，形成代理

/**
 * @Author ssw
 * @Time 2021.2.13
 * 该类的作用：
 * 处理标注 @RpcConsumer注解的接口，替换其BeanDefinition为其对应的BeanFactory的BeanDefinition(MyConsumerBeanFactory)
 * 这样用户在注入消费者接口时，spring会通过getBean()方法从容器中获取实例，此时会调用BeanFactory的getObject()方法
 * 然后getObject()方法会通过反射，生成该接口的代理。
 * 该代理会通过RpcClient远程调用，并返回结果
 */
public class ConsumerBeanDefinitionConfig implements ImportBeanDefinitionRegistrar {

    //private String SCANNER_PATH = "com.ssw.myRpc";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        //获得EnableMyRpcClient注解的所有属性，annotationMetadata表示标注@EnableMyRpcClient注解的类
        Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(EnableMyRpcClient.class.getName(), true);
        assert attributes != null;
        if (attributes.containsKey("scanPath")) {
            //获取EnableMyRpcClients的scanPath注解
            Object scanPath = attributes.get("scanPath");

            //ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
            ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false){
                protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                    boolean isCandidate = false;
                    if (beanDefinition.getMetadata().isIndependent() && !beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }

                    return isCandidate;
                }
            };
            //过滤保留只有标注了RpcConsumer注解的BeanDefinition
            AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(RpcConsumer.class);
            //AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(Controller.class);
            scanner.addIncludeFilter(annotationTypeFilter);
            //scanner.addExcludeFilter(annotationTypeFilter);
            Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(scanPath.toString());
            for (BeanDefinition beanDefinition : beanDefinitions) {
                //获取标注该注解的接口的beanName
                String beanName = beanDefinition.getBeanClassName();
                //获取该接口标注的@RpcConsumer注解中的属性,将BeanDefinition强转为AnnotationBeanDefinition
                if (beanDefinition instanceof AnnotatedBeanDefinition) {
                    AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) beanDefinition;
                    //获取BeanDefinition的元信息，即原来的接口信息
                    AnnotationMetadata metadata = annotatedBeanDefinition.getMetadata();
                    //判断是否为接口，不是接口，标准RpcConsumer则报错
                    //Assert.isTrue(!metadata.isInterface(), "@RpcConsumer can only be specified on an interface");
                    //获取该接口标注的RpcConsumer注解中含有的属性，即远程调用的服务名和负载均衡策略
                    Map<String, Object> rpcConsumerAttr = metadata.getAnnotationAttributes(RpcConsumer.class.getName(),true);


//                    if(rpcConsumerAttr != null){
//                        System.out.println(beanName);
//                        System.out.println("xxx");
//                        for (Map.Entry<String, Object> stringObjectEntry : rpcConsumerAttr.entrySet()) {
//                            System.out.println(stringObjectEntry.getKey() + ":" + stringObjectEntry.getValue());
//                        }
//                    }


                    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MyConsumerBeanFactory.class);
                    //使用beanFactory替代原本的BeanDefinition，这样使用@Autowired自动注入时，注入的是beanFactory通过getObject()得到的代理
                    //因为@Autowired底层使用了getBean()方法，该方法获取Bean时会判断对应的Object是否实现了FactoryBean接口
                    AbstractBeanDefinition beanFactoryDefinition = beanDefinitionBuilder.getBeanDefinition();

                    //将标注RpcConsumer注解的接口Class和RpcConsumer注解中的属性作为参数加入到BeanFactory中，以便使用反射构建代理对象
                    try {
                        Class<?> aClass = Class.forName(beanName);
                        beanFactoryDefinition.getConstructorArgumentValues().addGenericArgumentValue(aClass);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    beanFactoryDefinition.getConstructorArgumentValues().addGenericArgumentValue(rpcConsumerAttr);

                    //将包装好的BeanFactoryDefinition替换原来接口的BeanDefinition，这样就可以被Spring实例化了
                    registry.registerBeanDefinition(beanName, beanFactoryDefinition);
                }
            }
        }
    }
}
