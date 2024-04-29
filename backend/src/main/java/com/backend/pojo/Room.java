package com.backend.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class Room {
    private String roomId;//房间号
    private String roomType;//房间类型
    private String checkinDate;//入住时间
    private String customerId;//入住人身份证
    private String customerGender;//入住人性别
    private boolean checkinStatus;//入住状态
}
