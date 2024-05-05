package com.backend.pojo;

import lombok.Data;

@Data
public class Customer {
    /**
     * 电话号码
     */
    private String contactNumber;
    /**
     * 性别
     */
    private String customerGender;
    /**
     * 身份证号
     */
    private String customerId;
    /**
     * 名字
     */
    private String customerName;
}
