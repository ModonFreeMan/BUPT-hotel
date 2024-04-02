package com.backend.pojo;

import lombok.Data;

@Data
public class Customer {
    private String customerId;//顾客身份证
    private String contactNumber;//联系方式
    private String customerGender;//顾客性别
    private String customerName;//顾客姓名
    private String roomId;//房间号
}
