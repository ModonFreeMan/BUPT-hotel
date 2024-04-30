package com.backend.pojo;

import lombok.Data;

@Data
public class Statistics {
    /**
     * 天数1-7,日报和周报
     */
    private long period;
    /**
     * 详单条数
     */
    private long detailedBillSum;
    /**
     * 调度次数
     */
    private long dispatchSum;
    /**
     * 请求时长（等待的时长）
     */
    private long requestLength;
    /**
     * 房间号(-1为酒店总的，其他为每个房间)
     */
    private String roomId;
    /**
     * 调风次数
     */
    private long speedChangeSum;
    /**
     * 开关次数
     */
    private long switchSum;
    /**
     * 调温次数
     */
    private long temChangeSum;
    /**
     * 总费用
     */
    private double totalFee;
}
