package com.backend.service.Impl;

import com.backend.pojo.ACServiceObject;
import com.backend.pojo.AirConditionerStatus;
import com.backend.pojo.CentralACStatus;
import com.backend.service.CentralACService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CentralACServiceImpl implements CentralACService {

    @Autowired
    @Qualifier("CentralACStatus")
    private CentralACStatus centralACStatus;

    @Autowired
    @Qualifier("ACServiceMap")
    private ConcurrentHashMap<String, ACServiceObject> ACServiceMap;


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
        for(Map.Entry<String,ACServiceObject> entry: ACServiceMap.entrySet()){
            if(entry.getValue().isSwitchStatus()){
                return false;
            }
        }
        return true;
    }

    @Override
    public void changeCentralACStatus(CentralACStatus centralACState) {
        centralACStatus = centralACState;
    }
}
