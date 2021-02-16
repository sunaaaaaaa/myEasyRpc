package com.ssw.myRpc.loadbalance;

public class SelectRandom implements MyLoadBalance {
    @Override
    public int select(int amount) {
        return 0;
    }
}
