package com.backend.service.Impl;

import com.backend.mapper.RoomMapper;
import com.backend.mapper.TotalBillMapper;
import com.backend.pojo.*;
import com.backend.service.ReceptionService;
import com.backend.utils.SnowFlakeUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Service
public class ReceptionServiceImpl implements ReceptionService {

    @Resource
    RoomMapper roomMapper;

    @Resource
    TotalBillMapper totalBillMapper;

    //存储当前不空闲的房间对应的顾客和服务Id
    static List<UniqueServiceObject> uniqueServiceObjects = new LinkedList<>();

    /**
     * 登记入住信息
     * @param checkinRequest 用户对象
     */
    @Override
    public void checkIn(CheckinRequest checkinRequest) {
        Room room = new Room();
        room.setRoomId(checkinRequest.getRoomId());
        room.setCustomerGender(checkinRequest.getCustomerGender());
        room.setCustomerId(checkinRequest.getCustomerId());
        room.setCheckinStatus(true);
        try {
            roomMapper.updateRoom(room);
            //生成UniqueServiceObject，存到内存里
            String serviceId = SnowFlakeUtil.getSnowStr();
            UniqueServiceObject uniqueService = new UniqueServiceObject();
            uniqueService.setServiceId(serviceId);
            uniqueService.setCustomerId(checkinRequest.getCustomerId());
            uniqueService.setRoomId(checkinRequest.getRoomId());
            uniqueServiceObjects.add(uniqueService);

            //todo：将顾客信息存入CustomersTable
            //todo：创建一个空调服务对象？？
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询总账单对象
     */
    @Override
    public TotalBill getBill(String serviceId) {
        return totalBillMapper.getTotalBillByServiceId(serviceId);
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
        Integer id = Integer.getInteger(roomId);
        if(id <= 0 || id > 20){
            //暂时设定房间数量为20
            System.out.println("房间号错误");
            return false;
        }
        Room room = roomMapper.getRoom(roomId);
        return room.isCheckinStatus();
    }

    /**
     * 获取所有数据库房间对象
     */
    @Override
    public List<Room> getAllRoomsInfo() {
        return roomMapper.roomList();
    }

    /**
     * 查询当前所有服务的房间中是否服务该顾客
     */
    @Override
    public String isCustomerExist(String roomId,String customerId) {
        UniqueServiceObject uniqueService = getUniqueService(roomId);
        if(uniqueService == null)
            return "";
        if(!uniqueService.getCustomerId().equals(customerId))
            return "";
        return uniqueService.getServiceId();
    }


    @Override
    public void checkOut(String roomId,String serviceId) {
        Room room = roomMapper.getRoom(roomId);
        String inDate = room.getCheckinDate();
        // 将字符串转换为LocalDateTime对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime inTime = LocalDateTime.parse(inDate, formatter);
        // 获取当前日期和时间
        LocalDateTime nowTime = LocalDateTime.now();
        // 计算两个时间之间的日期数
        Duration duration = Duration.between(inTime, nowTime);
        int days = (int)duration.toDays();
        //todo：查询详单
        TotalBill totalBill = new TotalBill();
        totalBill.setServiceId(serviceId);
        totalBill.setDays((int)days);
        //todo：填入TotalBills
    }


    /**
     * 待实现
     */
    @Override
    public List<DetailedBill> getDetailedBills(String serviceId) {
        //todo:查询详单表
        return null;
    }

    /**
     * 待实现
     */
    @Override
    public Proof getProof(String serviceId, double paid) {
        TotalBill totalBill = totalBillMapper.getTotalBillByServiceId(serviceId);

        Proof proof = new Proof();
        proof.setServiceId(serviceId);
        proof.setPaid(paid);
        proof.setPayable(totalBill.getTotalFee());
        proof.setChange(paid-totalBill.getTotalFee());
        proof.setRoomId(totalBill.getRoomId());
        //todo:根据customerId查询顾客姓名并填入
        return proof;
    }

    /**
     * 根据房间号，从内存中获取ServiceId
     */
    @Override
    public String getServiceId(String roomId) {
        Integer id = Integer.getInteger(roomId);
        if(id <= 0 || id > 20){
            //暂时设定房间数量为20
            System.out.println("房间号错误");
            return "";
        }
        UniqueServiceObject uniqueService = getUniqueService(roomId);
        if(uniqueService == null)
            return "";
        return uniqueService.getServiceId();
    }

    /**
     * 根据房间号获取UniqueServiceObject
     */
    public static UniqueServiceObject getUniqueService(String roomId){
        for (UniqueServiceObject e: uniqueServiceObjects
        ) {
            if(e.getRoomId().equals(roomId))
                return e;
        }
        return null;
    }
}
