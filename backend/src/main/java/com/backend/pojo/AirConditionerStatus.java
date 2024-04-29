package com.backend.pojo;

import lombok.Data;

@Data
public class AirConditionerStatus {
    private double curTem;//当前温度
    private String roomId;//当前空调所属房间号
    private boolean runningStatus;//运行状态
    private long speedLevel;//风速
    private boolean switchStatus;//开关状态
    private double targetTem;//目标温度
}
