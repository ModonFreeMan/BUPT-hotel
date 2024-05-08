package com.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetailedBill {
    /**
     * 结束温度
     */
    private double endTem;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 总费用
     */
    private double fee;
    /**
     * 费率
     */
    private double rate;
    /**
     * 房间号
     */
    private String roomId;
    /**
     * 风速
     */
    private int speedLevel;
    /**
     * 起始温度
     */
    private double startTem;
    /**
     * 开始时间,开始服务的时间
     */
    private String startTime;
    /**
     * 请求时间
     */
    private String requestTime;
    /**
     * 服务时长
     */
    private String serviceLength;
}
