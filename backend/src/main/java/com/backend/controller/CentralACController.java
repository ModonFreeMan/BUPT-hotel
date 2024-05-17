package com.backend.controller;

import com.backend.pojo.CentralACStatus;
import com.backend.pojo.Result;
import com.backend.service.CentralACService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

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
        return Result.success(centralACService.getAllACInfo());
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
