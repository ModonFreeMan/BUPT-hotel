package com.backend.controller;

import com.backend.pojo.AirConditionerRequest;
import com.backend.pojo.Result;
import com.backend.service.RoomsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/rooms")
public class RoomsController {

    @Resource
    private RoomsService roomsService;

    /**
     * 处理发来的空调请求
     * @param request 空调请求
     */
    @PutMapping("/request")
    public Result processRequest(@RequestBody AirConditionerRequest request){
        if(!roomsService.isTemperatureValid(request)){
            return Result.error("空调温度范围不合法");
        }
        if(roomsService.isNeedProcess(request))// 合法且需要处理
            roomsService.processRequest(request);
        return Result.success();
    }

    /**
     * 获取当前空调状态信息
     * @param roomId 房间号
     * @return 房间空调状态
     */
    @GetMapping("/status/{roomId}")
    public Result getAirConditionerStatus(@PathVariable String roomId){
        return Result.success(roomsService.getAirConditionerStatus(roomId));
    }



}
