package com.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
