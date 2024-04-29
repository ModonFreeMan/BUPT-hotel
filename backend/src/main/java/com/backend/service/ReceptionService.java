package com.backend.service;

import com.backend.pojo.Customer;
import com.backend.pojo.Room;
import com.backend.pojo.TotalBill;

import java.util.List;

public interface ReceptionService {
    //顾客登记
    void checkin(Customer customer);
    //获取总账单
    TotalBill getBill(String serviceId);

    boolean findById(String serviceId);

    boolean isRoomEmpty(String roomId);

    List<Room> getAllRoomsInfo();
}
