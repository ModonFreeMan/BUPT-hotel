package com.backend.pojo;

import lombok.Data;

@Data
public class Room {
    /**
     * 入住日期
     */
    private String checkinDate;
    /**
     * 1入住/0未入住
     */
    private boolean checkinStatus;
    /**
     * 顾客性别
     */
    private String customerGender;
    /**
     * 顾客ID
     */
    private String customerId;
    /**
     * 顾客名称
     */
    private String customerName;
    /**
     * 房间ID
     */
    private String roomId;
    /**
     * 房间类型
     */
    private String roomType;
    /**
     * 服务ID
     */
    private String serviceId;
}
