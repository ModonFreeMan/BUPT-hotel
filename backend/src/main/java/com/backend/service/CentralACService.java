package com.backend.service;

import com.backend.pojo.AirConditionerStatus;
import com.backend.pojo.CentralACStatus;

import java.util.List;

public interface CentralACService {
    List<AirConditionerStatus> getAllACInfo();

    CentralACStatus getCentralACStatus();

    boolean isACAllOff();

    void changeCentralACStatus(CentralACStatus centralACStatus);
}
