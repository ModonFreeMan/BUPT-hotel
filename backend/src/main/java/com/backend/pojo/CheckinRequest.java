package com.backend.pojo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CheckinRequest {
    /**
     * 电话号码
     */
    @NotEmpty
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
    /**
     * 房间号
     */
    private String roomId;
}
