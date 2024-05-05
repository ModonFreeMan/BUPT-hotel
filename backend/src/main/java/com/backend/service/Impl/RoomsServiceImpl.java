package com.backend.service.Impl;

import com.backend.pojo.ACServiceObject;
import com.backend.pojo.AirConditionerRequest;
import com.backend.pojo.AirConditionerStatus;
import com.backend.pojo.CentralACStatus;
import com.backend.service.RoomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomsServiceImpl implements RoomsService {

    @Autowired
    @Qualifier("ACServiceMap")
    private ConcurrentHashMap<String, ACServiceObject>  ACServiceMap;

    @Autowired
    @Qualifier("CentralACStatus")
    private CentralACStatus centralACStatus;

    @Override
    public String getServiceId(String roomId) {
        return null;
    }


    @Override
    public AirConditionerStatus getAirConditionerStatus(String roomId) {
        return null;
    }


    @Override
    public boolean isTemperatureValid(AirConditionerRequest request) {
        return false;
    }

    @Override
    public void processRequest(AirConditionerRequest request) {
        //检查switchStatus
    }


}
