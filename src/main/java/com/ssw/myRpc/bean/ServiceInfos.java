package com.ssw.myRpc.bean;

import java.util.ArrayList;
import java.util.List;

public class ServiceInfos {
    private List<ServiceInfo> serviceInfos = new ArrayList<>();

    public List<ServiceInfo> getServiceInfos() {
        return serviceInfos;
    }

    public void setServiceInfos(List<ServiceInfo> serviceInfos) {
        this.serviceInfos = serviceInfos;
    }
}
