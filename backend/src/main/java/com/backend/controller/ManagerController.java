package com.backend.controller;

import com.backend.pojo.Result;
import com.backend.pojo.Statistics;
import com.backend.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
        List<Statistics> statistics = managerService.getStatistics(startDate, endDate);
        List<Statistics> results = new ArrayList<>();
        for(Statistics statistic: statistics){
            Statistics result = new Statistics(
                    statistic.getDate(),
                    statistic.getDetailedBillSum(),
                    statistic.getDispatchSum(),
                    statistic.getRequestLength(),
                    statistic.getRoomId(),
                    statistic.getSpeedChangeSum(),
                    statistic.getSwitchSum(),
                    statistic.getTemChangeSum(),
                    Math.round(statistic.getTotalFee()*100)*0.01d
            );
            results.add(result);
        }
        return Result.success(results);
    }
}
