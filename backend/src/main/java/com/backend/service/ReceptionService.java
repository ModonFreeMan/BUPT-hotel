package com.backend.service;

import com.backend.pojo.*;

import java.util.List;

public interface ReceptionService {
    //顾客登记
    void checkIn(CheckinRequest checkinRequest);
    //获取总账单
    TotalBill getBill(String serviceId);

    boolean isRoomEmpty(String roomId);

    List<Room> getAllRoomsInfo();

    String isCustomerExist(String roomId,String customerId);

    void checkOut(String roomId,String serviceId);


    List<DetailedBill> getDetailedBills(String serviceId);

    Proof getProof(String serviceId, double paid);

    String getServiceId(String roomId);
}
