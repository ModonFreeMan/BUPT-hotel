package com.backend.service;

import com.backend.pojo.AirConditionerRequest;
import com.backend.pojo.AirConditionerStatus;

public interface RoomsService {

    AirConditionerStatus getAirConditionerStatus(String roomId);

    boolean isTemperatureValid (AirConditionerRequest request);

    boolean isNeedProcess(AirConditionerRequest request);

    void processRequest(AirConditionerRequest request);

    boolean enterServiceQueue();

    void leaveServiceQueue(String roomId, int leaveStatus);

    void enterWaitQueue(String roomId);

    String timeTrans(String oldTime, int offset);

    boolean isRunning(String roomId);
}
