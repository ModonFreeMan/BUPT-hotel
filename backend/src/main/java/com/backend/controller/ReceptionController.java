package com.backend.controller;


import com.backend.pojo.*;
import com.backend.service.ReceptionService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reception")
public class ReceptionController {
    @Resource
    private ReceptionService receptionService;


    /**
     * 办理入住
     * @param checkinRequest 顾客信息
     * @return 办理结果
     */
    @PostMapping("/checkin")
    public Result customerCheckIn(@RequestBody @Validated CheckinRequest checkinRequest) {
        //检查用户要入住的房间是否可用
        if (!receptionService.isRoomEmpty(checkinRequest.getRoomId())) {
            //如果可用，办理入住登记
            receptionService.checkIn(checkinRequest);
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
     * @param checkoutRequest 结账请求
     * @return 返回房间信息
     */
    @PutMapping("/checkout")
    public Result customerCheckOut(@RequestBody CheckoutRequest checkoutRequest) {
        String serviceId = receptionService.isCustomerExist(checkoutRequest.getRoomId(),checkoutRequest.getCustomerId());
        if(serviceId.isEmpty()){
            //该房间不存在或不服务该用户，打印错误信息
            return Result.error("信息不匹配");
        }
        TotalBill totalBill = receptionService.checkOut(checkoutRequest.getRoomId(),serviceId);
        if(totalBill == null){
            return Result.error("信息不匹配");
        }
        TotalBill result = new TotalBill(
                Double.parseDouble(String.format("%.2f",totalBill.getAcFee())),
                totalBill.getCustomerId(),
                totalBill.getCustomerName(),
                totalBill.getDays(),
                Double.parseDouble(String.format("%.2f",totalBill.getRoomFee())),
                totalBill.getRoomId(),
                totalBill.getRoomType(),
                totalBill.getServiceId(),
                Double.parseDouble(String.format("%.2f",totalBill.getTotalFee()))
        );
        return Result.success(result);
    }



    /**
     * 获取详单列表
     * @param roomId 房间号
     * @return 本次详单条目
     */
    @GetMapping("/details/{roomId}")
    public Result getDetailedBill(@PathVariable String roomId){
        //根据房间号获取服务号
        String serviceId = receptionService.getServiceId(roomId);
        System.out.println(serviceId);
        if(serviceId.isEmpty())
            return Result.error("信息不匹配");
        //根据服务号获取本次服务的所有详单
        List<DetailedBill> results = new ArrayList<>();
        List<DetailedBill> detailedBills = receptionService.getDetailedBills(serviceId);
        for(DetailedBill detailedBill : detailedBills){
            DetailedBill result = new DetailedBill(
                    detailedBill.getServiceId(),
                    Double.parseDouble(String.format("%.2f",detailedBill.getEndTem())),
                    detailedBill.getEndTime(),
                    Double.parseDouble(String.format("%.2f",detailedBill.getTotalFee())),
                    detailedBill.getRate(),
                    detailedBill.getRoomId(),
                    detailedBill.getSpeedLevel(),
                    Double.parseDouble(String.format("%.2f",detailedBill.getStartTem())),
                    detailedBill.getStartTime(),
                    detailedBill.getRequestTime(),
                    detailedBill.getServiceLength()
            );
            results.add(result);
        }
        return Result.success(results);
    }

    /**
     * 缴费及出具凭据
     * @param roomId 房间号
     * @param paid 实付
     * @return 凭据
     */
    @PutMapping("/proof")
    public Result getProof(@RequestParam(value="roomId")String roomId,@RequestParam(value="paid")double paid){
        String serviceId = receptionService.getServiceId(roomId);
        if(serviceId.isEmpty())
            return Result.error("信息不匹配");
        Proof proof = receptionService.getProof(serviceId,paid);
        Proof result = new Proof(
                proof.getChange(),
                proof.getCustomerName(),
                proof.getPaid(),
                proof.getPayable(),
                proof.getRoomId(),
                proof.getServiceId()
                );
        return Result.success(result);
    }


}
