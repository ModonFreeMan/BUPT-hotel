package com.backend.service;

import com.backend.pojo.*;

import java.util.List;

public interface ReceptionService {
    //顾客登记
    void checkIn(Customer customer);
    //获取总账单
    TotalBill getBill(String serviceId);

    boolean findById(String serviceId);

    boolean isRoomEmpty(String roomId);

    List<Room> getAllRoomsInfo();

    /**
     * 检查用户信息是否存在
     * @param customer
     * @return 存在返回true，不存在返回false
     */
    boolean isCustomerExist(Customer customer);

    void checkOut(String serviceId);

    String getServiceId(Customer customer);

    List<DetailedBill> getDetailedBills(String serviceId);

    Proof getProof(String serviceId, double paid);
}
