package com.backend.pojo;

import lombok.Data;

@Data
public class AirConditionerBill {
    private String serviceId;//全局服务标识
    private Integer speedLevel;//风速
    private String startTime;//开始时间
    private String endTime;//结束时间
    private Double startTem;//开始温度
    private Double endTem;//结束温度
    private String rate;//计费费率
    private Double fee;//产生费用
}
