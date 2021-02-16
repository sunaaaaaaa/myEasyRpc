package com.ssw.myRpc.loadbalance;

public class DefaultBalance implements MyLoadBalance {
    @Override
    public int select(int amount) {
        return 0;
    }
}
