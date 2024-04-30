package com.backend.service.Impl;

import com.backend.pojo.AirConditionerRequest;
import com.backend.pojo.AirConditionerStatus;
import com.backend.service.RoomsService;
import org.springframework.stereotype.Service;

@Service
public class RoomsServiceImpl implements RoomsService {
    @Override
    public String getServiceId(String roomId) {
        return null;
    }

    @Override
    public void processRequest(AirConditionerRequest request) {

    }

    @Override
    public AirConditionerStatus getAirConditionerStatus(String roomId) {
        return null;
    }
}
