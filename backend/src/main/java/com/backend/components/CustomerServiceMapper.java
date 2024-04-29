package com.backend.components;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomerServiceMapper {
    private Map<String, String> customerServiceMap = new ConcurrentHashMap<>();

    @Bean
    public Map<String, String> CustomerServiceMapper() {
        return customerServiceMap;
    }

    public String getServiceId(String roomId) {
        return customerServiceMap.get(roomId);
    }

    public void addServiceId(String roomId, String serviceId) {
        customerServiceMap.put(roomId, serviceId);
    }

    public void removeServiceId(String roomId) {
        customerServiceMap.remove(roomId);
    }
}
