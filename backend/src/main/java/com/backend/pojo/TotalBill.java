package com.backend.pojo;

import lombok.Data;

@Data
public class TotalBill {
    /**
     * 空调费用
     */
    private double acFee;
    /**
     * 顾客姓名
     */
    private String customerName;
    /**
     * 入住天数
     */
    private long days;
    /**
     * 住宿费
     */
    private double roomFee;
    /**
     * 房间号
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
    /**
     * 总费用
     */
    private double totalFee;


    public TotalBill() {
    }

    public TotalBill(String serviceId, CheckinRequest checkinRequest) {
        this.serviceId = serviceId;
        this.customerName = checkinRequest.getCustomerName();
        this.roomId = checkinRequest.getRoomId();
    }
}
