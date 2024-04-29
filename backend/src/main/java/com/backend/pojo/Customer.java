package com.backend.pojo;

import com.backend.anno.CustomerId;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Customer {
    @CustomerId
    private String customerId;//顾客身份证
    @NotEmpty
    private String contactNumber;//联系方式

    private String customerGender;//顾客性别
    private String customerName;//顾客姓名
    private String roomId;//房间号
}
