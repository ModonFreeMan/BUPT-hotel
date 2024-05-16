package com.backend.service.Impl;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.backend.mapper.CustomerMapper;
import com.backend.mapper.DetailedBillMapper;
import com.backend.mapper.RoomMapper;
import com.backend.mapper.TotalBillMapper;
import com.backend.pojo.*;
import com.backend.service.ReceptionService;
import com.backend.utils.SnowFlakeUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReceptionServiceImpl implements ReceptionService {

    @Resource
    RoomMapper roomMapper;

    @Resource
    CustomerMapper customerMapper;

    @Resource
    TotalBillMapper totalBillMapper;

    @Resource
    DetailedBillMapper detailedBillMapper;

    @Resource
    CentralACStatus centralACStatus; //中央空调状态

    @Resource(name = "ACServiceMap")
    ConcurrentHashMap<String, ACServiceObject> acServiceObjects; //房间空调状态

    @Resource(name = "FiveRoomDetailsMap")
    HashMap<String, FiveRoomDetail> fiveRoomDetailHashMap; //房间配置信息

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
        LocalDate nowTime = LocalDate.now();
        room.setCheckinDate(String.valueOf(nowTime));
        try {
            roomMapper.updateRoom(room);
            //生成UniqueServiceObject，存到内存里
            String serviceId = SnowFlakeUtil.getSnowStr();
            UniqueServiceObject uniqueService = new UniqueServiceObject();
            uniqueService.setServiceId(serviceId);
            uniqueService.setCustomerId(checkinRequest.getCustomerId());
            uniqueService.setRoomId(checkinRequest.getRoomId());
            uniqueServiceObjects.add(uniqueService);
            String customerName = customerMapper.selectNameById(checkinRequest.getCustomerId());
            if(customerName != null && !customerName.isEmpty())
                System.out.println("用户"+customerName+"已经注册过");
            else {
                Customer customer = new Customer(checkinRequest.getContactNumber(),checkinRequest.getCustomerGender(),checkinRequest.getCustomerId(),checkinRequest.getCustomerName());
                customerMapper.insertCustomer(customer);
            }
            //每次入住时创建新的空调服务对象
            ACServiceObject acServiceObject = new ACServiceObject();
            acServiceObject.setSwitchStatus(false);//空调初始情况为关机
            acServiceObject.setCurTem(fiveRoomDetailHashMap.get(checkinRequest.getRoomId()).getInitialTem());
            acServiceObject.setSpeedLevel(2); //空调初始风速为中速
            acServiceObject.setWorkMode(centralACStatus.isWorkMode());// 空调初始工作模式与中央空调一致
            acServiceObject.setDays(1);
            acServiceObjects.put(checkinRequest.getRoomId(),acServiceObject); //存入空调状态map
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
     * 查询房间是否空闲
     * @param roomId 房间号
     */
    @Override
    public boolean isRoomEmpty(String roomId) {
        Room room = roomMapper.getRoom(roomId);
        return room.isCheckinStatus();
    }

    /**
     * 获取所有数据库房间对象
     */
    @Override
    public List<Room> getAllRoomsInfo() {
        List<Room> rooms = roomMapper.roomList();
        for (Room room:rooms) {
            String customerId = room.getCustomerId();
            String customerName = customerMapper.selectNameById(customerId);
            room.setCustomerName(customerName);
            UniqueServiceObject uniqueService = getUniqueService(room.getRoomId());
            if(uniqueService!= null)
                room.setServiceId(uniqueService.getServiceId());
        }
        return rooms;

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
    public TotalBill checkOut(String roomId,String serviceId) {
        UniqueServiceObject uniqueServiceObject = getUniqueService(roomId);
        if(uniqueServiceObject == null){
            System.out.println("退房时出错！不服务该用户");
            return null;
        }
        TotalBill totalBill = new TotalBill();
        totalBill.setServiceId(serviceId);
        totalBill.setRoomId(roomId);
        totalBill.setCustomerId(uniqueServiceObject.getCustomerId());
        totalBill.setCustomerName(customerMapper.selectNameById(uniqueServiceObject.getCustomerId()));
        Room room = roomMapper.getRoom(roomId);
        /*
        //计算入住时间。
        String inDate = room.getCheckinDate();
        // 将字符串转换为LocalDate对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate inTime = LocalDate.parse(inDate, formatter);
        LocalDate nowTime = LocalDate.now();
        // 计算两个时间之间的日期数
        //Duration duration = Duration.between(inTime, nowTime);
        //int days = (int)duration.toDays();
        int days = inTime.getDayOfMonth()-nowTime.getDayOfMonth();*/
        int days = acServiceObjects.get(roomId).getDays(); //改为从空调服务对象中获取天数
        totalBill.setDays(days);
        totalBill.setRoomType(room.getRoomType());
        double roomFee = days*fiveRoomDetailHashMap.get(roomId).getFeeEveryDay();
        totalBill.setRoomFee(roomFee);
        /*
        List<DetailedBill> detailedBills = getDetailedBills(serviceId);
        double acFee = 0;
        for (DetailedBill d: detailedBills) {
            acFee += d.getFee();
        }*/
        double acFee = acServiceObjects.get(roomId).getTotalFee();//改为从空调服务对象中获取总费用
        totalBill.setAcFee(acFee);
        totalBill.setTotalFee(roomFee+acFee);
        totalBillMapper.insertBill(totalBill);

        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("acFee","空调费用");
        stringMap.put("customerId","顾客身份证");
        stringMap.put("customerName","顾客姓名");
        stringMap.put("days","入住天数");
        stringMap.put("roomFee","住宿费");
        stringMap.put("roomId","房间号");
        stringMap.put("roomType","房间类型");
        stringMap.put("serviceId","服务ID");
        stringMap.put("totalFee","总费用");
        List<TotalBill> out = new LinkedList<>();//生成excel只能用集合类
        out.add(totalBill);
        generateExcel(out,stringMap,roomId,serviceId,"总账单表格.xlsx");
        return totalBill;
    }


    /**
     * 查询该次服务下所有详单
     */
    @Override
    public List<DetailedBill> getDetailedBills(String serviceId) {
        List<DetailedBill> detailedBills = detailedBillMapper.getDetailedBills(serviceId);
        /*ExcelWriter excelWriter = ExcelUtil.getWriter(true);
        excelWriter.setOnlyAlias(true);
        excelWriter.addHeaderAlias("serviceId","服务标识");
        excelWriter.write(detailedBills,true);
        try {
            OutputStream outputStream = new FileOutputStream(serviceId+"详单表格.xlsx");
            excelWriter.flush(outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("表格输出失败");
        }
        excelWriter.close();*/
        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("serviceId","服务标识");
        stringMap.put("endTem","结束温度");
        stringMap.put("endTime","结束时间");
        stringMap.put("fee","总费用");
        stringMap.put("rate","费率");
        stringMap.put("roomId","房间号");
        stringMap.put("speedLevel","风速");
        stringMap.put("startTem","起始温度");
        stringMap.put("startTime","开始时间");
        stringMap.put("requestTime","请求时间");
        stringMap.put("serviceLength","服务时长");
        generateExcel(detailedBills,stringMap,"",serviceId,"详单表格.xlsx");
        return detailedBills;
    }

    /**
     * 返回凭据对象
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
        proof.setCustomerName(totalBill.getCustomerName());
        //删去对应的UniqueObject
        deleteUniqueService(serviceId);
        roomMapper.setRoomFree(totalBill.getRoomId());

        TreeMap<String,String> stringMap = new TreeMap<>();
        stringMap.put("change","找余");
        stringMap.put("customerName","顾客姓名");
        stringMap.put("paid","实缴");
        stringMap.put("payable","应缴");
        stringMap.put("roomId","房间号");
        stringMap.put("serviceId","服务ID");

        List<Proof> out = new LinkedList<>();
        out.add(proof);
        generateExcel(out,stringMap,totalBill.getRoomId(),serviceId,"凭据表格.xlsx");

        return proof;
    }

    /**
     * 根据房间号，从内存中获取ServiceId
     */
    @Override
    public String getServiceId(String roomId) {
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

    /**
     * 根据服务号删除UniqueServiceObject
     */
    public static void deleteUniqueService(String serviceId){
        for (UniqueServiceObject e: uniqueServiceObjects
        ) {
            if(e.getServiceId().equals(serviceId)){
                uniqueServiceObjects.remove(e);
                break;
            }
        }
    }

    /**
     * 生成需要的excel文件
     */
    public void generateExcel(List<?> data,TreeMap<String,String> stringMap,String roomId,String serviceId,String fileSuffix){
        ExcelWriter excelWriter = ExcelUtil.getWriter(true);
        excelWriter.setOnlyAlias(true);
        Set<String> keySet = stringMap.keySet();
        for (String key:keySet) {
            excelWriter.addHeaderAlias(key,stringMap.get(key));
        }
        excelWriter.write(data,true);
        try {
            OutputStream outputStream = new FileOutputStream(roomId+"---"+serviceId+fileSuffix);
            excelWriter.flush(outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("表格输出失败");
        }
        excelWriter.close();
    }

}
