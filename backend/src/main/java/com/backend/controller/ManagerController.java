package com.backend.controller;

import com.backend.pojo.Result;
import com.backend.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    /**
     * 获取当前所有房间的统计信息
     * @param date 截至日期
     * @return 房间统计信息列表
     */
    @GetMapping("/statistic/{date}")
    public Result getStatistics(@PathVariable String date){
        return Result.success(managerService.getStatistics(date));
    }
}
