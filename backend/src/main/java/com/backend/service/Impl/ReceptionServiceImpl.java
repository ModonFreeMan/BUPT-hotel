package com.backend.service.Impl;

import com.backend.mapper.RoomMapper;
import com.backend.mapper.TotalBillMapper;
import com.backend.pojo.*;
import com.backend.service.ReceptionService;
import com.backend.utils.MybatisUtil;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceptionServiceImpl implements ReceptionService {
    @Resource
    MybatisUtil mybatisUtil;

    @Resource
    RoomMapper roomMapper;

    @Resource
    TotalBillMapper totalBillMapper;

    /**
     * 登记入住信息
     * @param customer 用户对象
     */
    @Override
    public void checkIn(Customer customer) {
        //todo：可能还需要向数据库中添加Customer表？

        //todo；应该还需要分配一个serviceId？还是说用了空调服务才收费，应该不是
        //todo：但是要生成吗？而且服务分段时serviceId一样的话，Bill会冲突吧？
        //todo：还是说分段的服务放进redis里？
        Room room = new Room();
        room.setRoomId(customer.getRoomId());
        room.setCustomerGender(customer.getCustomerGender());
        room.setCustomerId(customer.getCustomerId());
        room.setCheckinStatus(true);
        try {
            roomMapper.updateRoom(room);
            //入住成功，开始计费

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
        Room room = roomMapper.getRoom(roomId);
        return room.isCheckinStatus();
    }

    /**
     * 获取所有房间对象
     */
    @Override
    public List<Room> getAllRoomsInfo() {
        return roomMapper.roomList();
    }

    /**
     * 待实现
     */
    @Override
    public boolean isCustomerExist(Customer customer) {
        return false;
    }

    /**
     * 待实现
     */
    @Override
    public String getServiceId(Customer customer) {
        return totalBillMapper.getTotalBill(customer.getCustomerId()).getServiceId();
    }

    @Override
    public void checkOut(String serviceId) {

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
