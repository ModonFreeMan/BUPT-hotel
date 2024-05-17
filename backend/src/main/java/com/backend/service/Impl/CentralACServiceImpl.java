package com.backend.service.Impl;

import com.backend.mapper.RoomMapper;
import com.backend.pojo.*;
import com.backend.service.CentralACService;
import com.backend.service.RoomsService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CentralACServiceImpl implements CentralACService {

    @Autowired
    @Qualifier("CentralACStatus")
    private CentralACStatus centralACStatus;

    @Resource
    private RoomsService roomsService;

    @Resource
    private RoomMapper roomMapper;

    @Autowired
    @Qualifier("FiveRoomDetailsMap")
    private HashMap<String, FiveRoomDetail> fiveRoomDetailsMap;


    @Autowired
    @Qualifier("ACServiceMap")
    private ConcurrentHashMap<String, ACServiceObject> ACServiceMap;


    @Override
    public List<AirConditionerStatus> getAllACInfo() {
        List<AirConditionerStatus> airConditionerStatusList = new ArrayList<>();
        //遍历所有的房间
        for(Room room: this.roomMapper.roomList()){
            String roomId = room.getRoomId();
            AirConditionerStatus airConditionerStatus = new AirConditionerStatus();
            airConditionerStatus.setRoomId(roomId);
            if(ACServiceMap.containsKey(roomId)){
                ACServiceObject acServiceObject = ACServiceMap.get(roomId);
                airConditionerStatus.setCurTem(acServiceObject.getCurTem());
                airConditionerStatus.setSwitchStatus(acServiceObject.isSwitchStatus());
                airConditionerStatus.setTotalFee(acServiceObject.getTotalFee());
                airConditionerStatus.setRunningStatus(this.roomsService.isRunning(room.getRoomId()));
                airConditionerStatus.setCurrentFee(acServiceObject.getCurrentFee());
                airConditionerStatus.setWorkMode(centralACStatus.isWorkMode());
                airConditionerStatus.setSpeedLevel(acServiceObject.getSpeedLevel());
                airConditionerStatus.setTargetTem(acServiceObject.getTargetTem());
            }
            else {
                airConditionerStatus.setCurTem(this.fiveRoomDetailsMap.get(roomId).getInitialTem());
                airConditionerStatus.setSwitchStatus(false);
                airConditionerStatus.setTotalFee(0d);
                airConditionerStatus.setRunningStatus(false);
                airConditionerStatus.setCurrentFee(0d);
                airConditionerStatus.setWorkMode(centralACStatus.isWorkMode());
                airConditionerStatus.setSpeedLevel(2);
                if(centralACStatus.isWorkMode()){
                    airConditionerStatus.setTargetTem(25d);
                }
                else {
                    airConditionerStatus.setTargetTem(22d);
                }
            }
            airConditionerStatusList.add(airConditionerStatus);
        }
        return airConditionerStatusList;
    }

    @Override
    public CentralACStatus getCentralACStatus() {
        return this.centralACStatus;
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
        this.centralACStatus.setSwitchStatus(centralACState.isSwitchStatus());
        this.centralACStatus.setRate(centralACState.getRate());
        this.centralACStatus.setLowerBound(centralACState.getLowerBound());
        this.centralACStatus.setUpperBound(centralACState.getUpperBound());
        this.centralACStatus.setWorkMode(centralACState.isWorkMode());
    }
}
