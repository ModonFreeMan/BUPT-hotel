package com.backend.pojo;

import lombok.Data;

/**
 * 空调服务对象
 */
@Data
public class ACServiceObject {
    /**
     *  加入等待队列的时间
     */
    private String waiting_queue_timestamp;
    /**
     * 加入服务队列的时间戳
     */
    private String service_queue_timestamp;
    /**
     * 空调开关状态
     */
    private boolean switchStatus;
    /**
     * 当前房间温度
     */
    private double curTem;
    /**
     * 进入服务队列时的温度
     */
    private double beforeServiceTem;
    /**
     * 设定风速
     */
    private long speedLevel;
    /**
     * 目标温度
     */
    private double targetTem;
    /**
     * 0制冷/1制热
     */
    private boolean workMode;
    /**
     * 空调总费用
     */
    private double totalFee;
    /**
     * 本次开机费用
     */
    private double currentFee;

}
