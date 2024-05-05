package com.backend.pojo;

import lombok.Data;

@Data
public class Statistics {
    /**
     * 统计日期
     */
    private String date;
    /**
     * 详单条数
     */
    private int detailedBillSum;
    /**
     * 调度次数
     */
    private int dispatchSum;
    /**
     * 请求时长（等待的时长）
     */
    private int requestLength;
    /**
     * 房间号(-1为酒店总的，其他为每个房间)
     */
    private String roomId;
    /**
     * 调风次数
     */
    private int speedChangeSum;
    /**
     * 开关次数
     */
    private int switchSum;
    /**
     * 调温次数
     */
    private int temChangeSum;
    /**
     * 总费用
     */
    private double totalFee;
}
