package com.backend.controller;

import com.backend.pojo.Result;
import com.backend.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    /**
     * 获取当前所有房间的统计信息
     * @param startDate 开始日期
     * @param endDate   截止日期
     * @return 房间统计信息列表
     */
    @GetMapping("/statistic")
    public Result getStatistics(@RequestParam(name = "startDate", required = false) String startDate,
                                @RequestParam(name = "endDate", required = false) String endDate){
        return Result.success(managerService.getStatistics(startDate,endDate));
    }
}
