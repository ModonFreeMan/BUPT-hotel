package com.backend.controller;


import com.backend.components.CustomerServiceMapper;
import com.backend.pojo.Result;
import com.backend.pojo.TotalBill;
import com.backend.service.ReceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RoomsController {
    @Autowired
    private ReceptionService receptionService;


    @Autowired
    private CustomerServiceMapper customerServiceMapper;

    @GetMapping("/bill/{serviceId}")
    public Result getTotalBill(@PathVariable String serviceId){
        if(receptionService.findById(serviceId)){
            TotalBill totalBill = receptionService.getBill(serviceId);
            return Result.success(totalBill);
        }
        else{
            return Result.error("所要查询的服务不存在");
        }
    }

    @GetMapping("/detailedbill/{roomId}")
    public Result getDetailedBill(@PathVariable String roomId){
        return Result.success();
    }

}
