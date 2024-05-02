package com.backend.service.Impl;

import com.backend.mapper.RoomMapper;
import com.backend.pojo.*;
import com.backend.service.ReceptionService;
import com.backend.utils.MybatisUtil;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceptionServiceImpl implements ReceptionService {
    @Resource
    MybatisUtil mybatisUtil;

    /**
     * 登记入住信息
     * @param customer 用户对象
     */
    @Override
    public void checkIn(Customer customer) {
        //todo：可能还需要向数据库中添加Customer表？
        SqlSession session = mybatisUtil.getSession();
        RoomMapper roomMapper = session.getMapper(RoomMapper.class);
        Room room = new Room();
        room.setRoomId(customer.getRoomId());
        room.setCustomerGender(customer.getCustomerGender());
        room.setCustomerId(customer.getCustomerId());
        room.setCheckinStatus(true);
        try {
            roomMapper.updateRoom(room);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 待实现
     */
    @Override
    public TotalBill getBill(String serviceId) {
        return null;
    }

    /**
     * 待实现
     */
    @Override
    public boolean findById(String serviceId) {
        return false;
    }

    /**
     * 查询房间是否空闲
     * @param roomId 房间号
     */
    @Override
    public boolean isRoomEmpty(String roomId) {
        SqlSession session = mybatisUtil.getSession();
        RoomMapper roomMapper = session.getMapper(RoomMapper.class);

        Room room = roomMapper.getRoom(roomId);

        return room.isCheckinStatus();
    }

    /**
     * 获取所有房间对象
     */
    @Override
    public List<Room> getAllRoomsInfo() {
        SqlSession session = mybatisUtil.getSession();
        RoomMapper roomMapper = session.getMapper(RoomMapper.class);
        return roomMapper.roomList();
    }

    /**
     * 待实现
     */
    @Override
    public boolean isCustomerExist(Customer customer) {
        return false;
    }

    @Override
    public void checkOut(String serviceId) {

    }

    /**
     * 待实现
     */
    @Override
    public String getServiceId(Customer customer) {
        return null;
    }

    /**
     * 待实现
     */
    @Override
    public List<DetailedBill> getDetailedBills(String serviceId) {
        return null;
    }

    /**
     * 待实现
     */
    @Override
    public Proof getProof(String serviceId, double paid) {
        return null;
    }
}
