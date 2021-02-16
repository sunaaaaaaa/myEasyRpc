package com.ssw.myRpc.register.impl;

import com.ssw.myRpc.bean.Constant;
import com.ssw.myRpc.bean.ServiceInfo;
import com.ssw.myRpc.bean.ServiceInfos;
import com.ssw.myRpc.config.StarterProperties;
import com.ssw.myRpc.register.CallBackEvent;
import com.ssw.myRpc.register.MyRegister;
import com.ssw.myRpc.serializer.MySerializer;
import com.ssw.myRpc.serializer.SerializerEngine;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZkRegister implements MyRegister {

    private static Logger logger = LoggerFactory.getLogger(ZkRegister.class);

    private StarterProperties properties = Constant.properties.getProperties();
    private RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    private String zkString = properties.getRegisterIp() + ":" + properties.getRegisterPort(); //xxx.xxx.xxx.xxx:port
    private CuratorFramework client = CuratorFrameworkFactory.newClient(zkString, retryPolicy);
    private String COMMON_PATH = "/com/ssw/myRpc";
    private MySerializer serializer = SerializerEngine.getSerializer(properties.getSerializerType());

    public ZkRegister() {
        client.getConnectionStateListenable().addListener(((curatorFramework, connectionState) -> {
            switch (connectionState) {
                case CONNECTED:
                    logger.info("register client connect register success");
                    break;
                case RECONNECTED:
                    logger.info("register client retry connect register");
                    break;
                default:
                    break;
            }
        }));
        client.start();
    }

    @Override
    public void register(String serviceName, ServiceInfo list) throws Exception {
        String servicePath = COMMON_PATH +"/"+ serviceName;
        if (client.checkExists().forPath(servicePath) == null) {
            //节点不存在 ，这是该服务注册的第一个实例
            client.create().creatingParentsIfNeeded().forPath(servicePath);
            //List<ServiceInfo> value = new ArrayList<>();
            System.out.println("create node:" + servicePath);
            ServiceInfos value = new ServiceInfos();
            value.getServiceInfos().add(list);
            byte[] data = serializer.encoder(value);
            client.setData().forPath(servicePath, data);
        } else {
            //节点存在,说明该服务已经拥有一个实例了
            byte[] bytes = client.getData().forPath(servicePath);
            ServiceInfos data = serializer.decoder(bytes,ServiceInfos.class);
            data.getServiceInfos().add(list);
            client.setData().forPath(servicePath, serializer.encoder(data));
        }
    }

    @Override
    public void unRegister(String serviceName) {

    }

    @Override
    public List<ServiceInfo> findService(String serviceName) {

        return null;
    }

    @Override
    public Map<String, ServiceInfos> findAllServices() throws Exception {
        //获取myRpc父节点下的所有子节点路径
        List<String> childrenPaths = client.getChildren().forPath(COMMON_PATH);

        Map<String, ServiceInfos> res = new HashMap<>();
        for (String childrenPath : childrenPaths) {
            String path = COMMON_PATH +"/" + childrenPath;
            //获取子节点的数据
            byte[] bytes = client.getData().forPath(path);
            ServiceInfos data = serializer.decoder(bytes, ServiceInfos.class);
           // System.out.println(data.getServiceInfos().size());
            res.put(childrenPath, data);
        }
        return res;
    }

    @Override
    public void watch(CallBackEvent callBackEvent) {

    }

    @Override
    public void keepAlive() {

    }
}
