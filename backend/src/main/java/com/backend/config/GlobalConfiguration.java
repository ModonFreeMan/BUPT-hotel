package com.backend.config;

import com.backend.pojo.ACServiceObject;
import com.backend.pojo.CentralACStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class GlobalConfiguration {

    @Bean(name = "ACServiceMap")
    public ConcurrentHashMap<String, ACServiceObject> initACServiceMap() {
        // 在这里初始化空调服务对象列表
        return new ConcurrentHashMap<>();
    }

    @Bean(name = "CentralACStatus")
    public CentralACStatus initCentralACStatus() {
        // 在这里初始化中央空调对象
        CentralACStatus centralACStatus = new CentralACStatus();
        centralACStatus.setSwitchStatus(false);
        return centralACStatus;
    }


}
