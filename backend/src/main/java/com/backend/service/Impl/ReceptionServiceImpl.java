package com.backend.service.Impl;

import com.backend.pojo.Customer;
import com.backend.pojo.Room;
import com.backend.pojo.TotalBill;
import com.backend.service.ReceptionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceptionServiceImpl implements ReceptionService {


    /**
     *
     * @param customer 登记入住信息
     */
    @Override
    public void checkin(Customer customer) {

    }

    @Override
    public TotalBill getBill(String serviceId) {
        return null;
    }

    @Override
    public boolean findById(String serviceId) {
        return false;
    }

    @Override
    public boolean isRoomEmpty(String roomId) {
        return false;
    }

    @Override
    public List<Room> getAllRoomsInfo() {
        return null;
    }
}
