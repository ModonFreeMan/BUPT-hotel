package com.backend.pojo;

import lombok.Data;

@Data
public class Proof {
    /**
     * 找余
     */
    private double change;
    /**
     * 顾客姓名
     */
    private String customerName;
    /**
     * 实缴
     */
    private double paid;
    /**
     * 应缴
     */
    private double payable;
    /**
     * 房间号
     */
    private String roomId;
    /**
     * 服务ID
     */
    private String serviceId;

}
