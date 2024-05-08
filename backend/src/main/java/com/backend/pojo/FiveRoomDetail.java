package com.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author seaside
 * 2024-05-08 18:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiveRoomDetail {
    //为防止数据库字段频繁修改，先使用该类存储五个房间的初始温度和每日费用
    double initialTem;
    int feeEveryDay;
}
