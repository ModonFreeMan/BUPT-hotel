package com.backend.service;

import com.backend.pojo.AirConditionerRequest;
import com.backend.pojo.AirConditionerStatus;

public interface RoomsService {
    String getServiceId(String roomId);


    AirConditionerStatus getAirConditionerStatus(String roomId);


    boolean isTemperatureValid (AirConditionerRequest request);

    void processRequest(AirConditionerRequest request);
}
