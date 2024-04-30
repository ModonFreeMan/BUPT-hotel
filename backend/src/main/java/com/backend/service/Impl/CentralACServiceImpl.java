package com.backend.service.Impl;

import com.backend.pojo.AirConditionerStatus;
import com.backend.pojo.CentralACStatus;
import com.backend.service.CentralACService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CentralACServiceImpl implements CentralACService {
    @Override
    public List<AirConditionerStatus> getAllACInfo() {
        return null;
    }

    @Override
    public CentralACStatus getCentralACStatus() {
        return null;
    }

    @Override
    public boolean isACAllOff() {
        return false;
    }

    @Override
    public void changeCentralACStatus(CentralACStatus centralACStatus) {

    }
}
