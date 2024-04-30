package com.backend.controller;


import com.backend.components.CustomerServiceMapper;
import com.backend.pojo.Customer;
import com.backend.pojo.Result;
import com.backend.service.ReceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reception")
public class ReceptionController {
    @Autowired
    private ReceptionService receptionService;

    @Autowired
    private CustomerServiceMapper customerServiceMapper;



    @PostMapping("/checkin")
    public Result customerCheckIn(@RequestBody @Validated Customer customer){
        //检查用户的
        if(receptionService.isRoomEmpty(customer.getRoomId())){

            return Result.success();
        }else {
            return Result.error("房间已经入住");
        }
    }

    @GetMapping("/rooms-info")
    public Result getAllRoomsInfo(){
        return Result.success(receptionService.getAllRoomsInfo());
    }

    @PutMapping("/checkout")
    public Result customerCheckOut(@RequestBody Customer customer){
        if(!receptionService.isRoomEmpty(customer.getRoomId())){
            return Result.success();
        }else {
            return Result.error("房间信息错误, 用户尚未入住");
        }
    }








}
