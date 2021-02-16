package com.ssw.myRpc.loadbalance;

public class SelectFirstBalance implements MyLoadBalance {
    @Override
    public int select(int amount) {
        return 0;
    }
}
