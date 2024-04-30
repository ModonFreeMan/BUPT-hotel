package com.backend.pojo;

import lombok.Data;

@Data
public class AirConditionerStatus {
    /**
     * 当前费用
     */
    private double currentFee;
    /**
     * 当前温度
     */
    private double curTem;
    /**
     * 房间号
     */
    private String roomId;
    /**
     * 运行状态
     */
    private boolean runningStatus;
    /**
     * 当前风速
     */
    private long speedLevel;
    /**
     * 开关状态
     */
    private boolean switchStatus;
    /**
     * 目标温度
     */
    private double targetTem;
    /**
     * 总费用
     */
    private double totalFee;
    /**
     * 0制冷/1制热
     */
    private boolean workMode;
}