package com.backend.controller;

import com.backend.pojo.AirConditionerStatus;
import com.backend.pojo.CentralACStatus;
import com.backend.pojo.Result;
import com.backend.service.CentralACService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/centralAC")
public class CentralACController {

    @Resource
    private CentralACService centralACService;

    /**
     * 获取所有空调信息
     * @return 返回空调信息列表
     */
    @GetMapping("/room-info")
    public Result getAllACInfo(){
        List<AirConditionerStatus> airConditionerStatusList = centralACService.getAllACInfo();
        List<AirConditionerStatus> results = new ArrayList<>();
        for (AirConditionerStatus airConditionerStatus : airConditionerStatusList) {
            AirConditionerStatus result = new AirConditionerStatus(
                    Double.parseDouble(String.format("%.2f",airConditionerStatus.getCurrentFee())),
                    Double.parseDouble(String.format("%.2f",airConditionerStatus.getCurTem())),
                    airConditionerStatus.getRoomId(),
                    airConditionerStatus.isRunningStatus(),
                    airConditionerStatus.getSpeedLevel(),
                    airConditionerStatus.isSwitchStatus(),
                    airConditionerStatus.getTargetTem(),
                    Double.parseDouble(String.format("%.2f",airConditionerStatus.getTotalFee())),
                    airConditionerStatus.isWorkMode()
            );
            results.add(result);
        }
        return Result.success(results);
    }

    /**
     * 获取当前中央空调配置
     * @return 中央空调配置
     */
    @GetMapping("/status")
    public Result getCentralACStatus(){
        return Result.success(centralACService.getCentralACStatus());
    }

    /**
     * 修改中央空调配置
     * @param centralACStatus 中空空调配置
     * @return 修改操作成功或失败
     */
    @PutMapping("/control")
    public Result changeCentralACStatus(@RequestBody CentralACStatus centralACStatus){
        //判断当前所有空调是否全部关闭
        if(centralACService.isACAllOff()){
            centralACService.changeCentralACStatus(centralACStatus);
            return Result.success();
        }
        else {
            return Result.error("有空调正在运行，无法修改中央空调设置");
        }
    }



}
