package com.backend.pojo;

import lombok.Data;

@Data
public class AirConditionerRequest {
    private String roomId;
    /**
     * 风速
     */
    private long speedLevel;
    /**
     * 0关/1开
     */
    private boolean switchStatus;
    /**
     * 目标温度
     */
    private double targetTem;
}
