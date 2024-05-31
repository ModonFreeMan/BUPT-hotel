package com.backend.service;

import com.backend.pojo.*;

import java.util.List;

/**
 * 程序标题：ReceptionService
 * 模块描述：这个接口定义了前台服务的方法，包括顾客登记、获取总账单等功能。
 * 接口说明：这个接口的实现类可以被其他模块调用。
 * 负责人：温海斌 创建日期：2024/4/2
 */
public interface ReceptionService {
    /**
     * 程序标题：checkIn
     * 模块描述：这个方法用于顾客登记。
     * 接口说明：这个方法可以被其他模块调用，以进行顾客登记。
     * 负责人：温海斌
     */
    void checkIn(CheckinRequest checkinRequest);
    /**
     * 程序标题：getBill
     * 模块描述：这个方法用于获取总账单。
     * 接口说明：这个方法可以被其他模块调用，以获取总账单。
     * 负责人：温海斌
     */
    TotalBill getBill(String serviceId);

    /**
     * 程序标题：isRoomEmpty
     * 模块描述：这个方法用于检查房间是否为空。
     * 接口说明：这个方法可以被其他模块调用，以检查房间是否为空。
     * 负责人：温海斌
     */
    boolean isRoomEmpty(String roomId);

    /**
     * 程序标题：getAllRoomsInfo
     * 模块描述：这个方法用于获取所有房间的信息。
     * 接口说明：这个方法可以被其他模块调用，以获取所有房间的信息。
     * 负责人：温海斌
     */
    List<Room> getAllRoomsInfo();

    /**
     * 程序标题：isCustomerExist
     * 模块描述：这个方法用于检查顾客是否存在。
     * 接口说明：这个方法可以被其他模块调用，以检查顾客是否存在。
     * 负责人：温海斌
     */
    String isCustomerExist(String roomId,String customerId);

    /**
     * 程序标题：checkOut
     * 模块描述：这个方法用于顾客退房。
     * 接口说明：这个方法可以被其他模块调用，以进行顾客退房。
     * 负责人：温海斌
     */
    TotalBill checkOut(String roomId,String serviceId);


    /**
     * 程序标题：getDetailedBills
     * 模块描述：这个方法用于获取详细的账单。
     * 接口说明：这个方法可以被其他模块调用，以获取详细的账单。
     * 负责人：温海斌
     */
    List<DetailedBill> getDetailedBills(String serviceId);

    /**
     * 程序标题：getProof
     * 模块描述：这个方法用于获取付款凭证。
     * 接口说明：这个方法可以被其他模块调用，以获取付款凭证。
     * 负责人：温海斌
     */
    Proof getProof(String serviceId, double paid);

    /**
     * 程序标题：getServiceId
     * 模块描述：这个方法用于获取服务ID。
     * 接口说明：这个方法可以被其他模块调用，以获取服务ID。
     * 负责人：温海斌
     */
    String getServiceId(String roomId);
}
