package com.ssw.myRpc.register;

import com.ssw.myRpc.register.impl.EtcdRegister;
import com.ssw.myRpc.register.impl.ZkRegister;

import java.util.HashMap;
import java.util.Map;


public class RegisterHelper {

    private static Map<String, MyRegister> registerMap = new HashMap<>(8);


    static {
        registerMap.put("etcd", new EtcdRegister());
        registerMap.put("zookeeper", new ZkRegister());
        //registerMap.put("default",new DefaultRegister());
    }

    public static MyRegister getRegister(String curRegisterName) {
        if (registerMap.containsKey(curRegisterName)) {
            return registerMap.get(curRegisterName);
        } else {
            throw new RuntimeException("can support this type register" + curRegisterName);
        }
    }


}
