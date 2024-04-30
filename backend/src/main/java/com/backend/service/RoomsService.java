package com.backend.service;

import com.backend.pojo.AirConditionerRequest;
import com.backend.pojo.AirConditionerStatus;

public interface RoomsService {
    String getServiceId(String roomId);

    void processRequest(AirConditionerRequest request);

    AirConditionerStatus getAirConditionerStatus(String roomId);

    boolean isRequestValid(AirConditionerRequest request);
}
