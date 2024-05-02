package com.backend.controller;


import com.backend.pojo.Customer;
import com.backend.pojo.Result;
import com.backend.service.ReceptionService;
import com.backend.service.RoomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reception")
public class ReceptionController {
    @Autowired
    private ReceptionService receptionService;

    @Autowired
    private RoomsService roomsService;

    /**
     * 办理入住
     * @param customer 顾客信息
     * @return 办理结果
     */
    @PostMapping("/checkin")
    public Result customerCheckIn(@RequestBody @Validated Customer customer) {
        //检查用户要入住的房间是否可用
        if (!receptionService.isRoomEmpty(customer.getRoomId())) {
            //如果可用，办理入住登记
            receptionService.checkIn(customer);
            return Result.success();
        } else {
            //如果不可用返回错误信息
            return Result.error("房间不可用");
        }
    }

    /**
     * 前台获取所有房间信息
     * @return 所有房间信息列表
     */
    @GetMapping("/rooms-info")
    public Result getAllRoomsInfo() {
        //返回所有房间列表
        return Result.success(receptionService.getAllRoomsInfo());
    }

    /**
     * 为当前顾客办理结账手续
     * @param customer 顾客信息
     * @return 返回房间信息
     */
    @PutMapping("/checkout")
    public Result customerCheckOut(@RequestBody Customer customer) {
        //检查顾客信息是否存在，如果存在返回true
        if(receptionService.isCustomerExist(customer)){
            //如果顾客信息存在，获取本次serviceId
            String serviceId = receptionService.getServiceId(customer);
            //根据serviceId办理退房手续
            receptionService.checkOut(serviceId);
            return Result.success();
        }
        else {
            //否则打印错误信息
            return Result.error("用户信息不存在");
        }
    }

    /**
     * 获取详单列表
     * @param roomId 房间号
     * @return 本次详单条目
     */
    @GetMapping("/details/{roomId}")
    public Result getDetailedBill(@PathVariable String roomId){
        //根据房间号获取服务号
        String serviceId = roomsService.getServiceId(roomId);
        //根据服务号获取本次服务的所有详单
        return Result.success(receptionService.getDetailedBills(serviceId));
    }

    /**
     * 缴费及出具凭据
     * @param roomId 房间号
     * @param paid 实付
     * @return 凭据
     */
    @PutMapping("/proof")
    public Result getProof(@RequestParam(value="roomId")String roomId, @RequestParam(value="roomId")double paid){
        String serviceId = roomsService.getServiceId(roomId);
        return Result.success(receptionService.getProof(serviceId,paid));
    }

}
