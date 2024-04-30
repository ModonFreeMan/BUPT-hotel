package com.backend.service.Impl;

import com.backend.pojo.*;
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
    public void checkIn(Customer customer) {

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

    @Override
    public boolean isCustomerExist(Customer customer) {
        return false;
    }

    @Override
    public void checkOut(String serviceId) {

    }

    @Override
    public String getServiceId(Customer customer) {
        return null;
    }

    @Override
    public List<DetailedBill> getDetailedBills(String serviceId) {
        return null;
    }

    @Override
    public Proof getProof(String serviceId, double paid) {
        return null;
    }
}
