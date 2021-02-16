package com.ssw.myRpc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration("ssw-rpc-AutoConfig")
@EnableConfigurationProperties({StarterProperties.class, StarterClientProperties.class, StarterServerProperties.class})
public class StarterAutoConfig {

    @Autowired
    private StarterProperties properties;

    @Autowired
    private StarterClientProperties clientProperties;

    @Autowired
    private StarterServerProperties serverProperties;

    public StarterProperties getProperties() {
        return properties;
    }

    public void setProperties(StarterProperties properties) {
        this.properties = properties;
    }

    public StarterClientProperties getClientProperties() {
        return clientProperties;
    }

    public void setClientProperties(StarterClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }

    public StarterServerProperties getServerProperties() {
        return serverProperties;
    }

    public void setServerProperties(StarterServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }
}
