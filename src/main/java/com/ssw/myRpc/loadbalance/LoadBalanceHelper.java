package com.ssw.myRpc.loadbalance;

import java.util.HashMap;
import java.util.Map;

public class LoadBalanceHelper {

    private static Map<String, MyLoadBalance> loadBalanceMap = new HashMap<>(8);

    static {
        loadBalanceMap.put("default", new DefaultBalance());
        loadBalanceMap.put("first", new SelectFirstBalance());
        loadBalanceMap.put("random", new SelectRandom());
    }

    public static Map<String, MyLoadBalance> getLoadBalanceMap() {
        return loadBalanceMap;
    }
}
