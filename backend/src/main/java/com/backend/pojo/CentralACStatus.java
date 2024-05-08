package com.backend.pojo;

import lombok.Data;

@Data
public class CentralACStatus {
    /**
     * 0关闭/1开启
     */
    private boolean switchStatus;
    /**
     * 费率数组,低中高速风费率,下标分别是0，1，2，可直接用风速作为下标
     */
    private double rate;
    /**
     * 温度下界
     */
    private int lowerBound;
    /**
     * 0制冷/1制热
     */
    private boolean workMode;
    /**
     * 温度上界
     */
    private int upperBound;
}
