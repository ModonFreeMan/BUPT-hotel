package com.backend.pojo;

import lombok.Data;

@Data
public class TotalBill {
    private String contactNumber;//联系方式
    private String customerId;//身份证号
    private String customerGender;//性别
    private String customerName;//姓名
    private String serviceId;//全局唯一服务标识
    private Double totalFee;//本次产生总费用
}
