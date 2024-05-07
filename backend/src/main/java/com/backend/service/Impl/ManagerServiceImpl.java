package com.backend.service.Impl;

import com.backend.mapper.RoomMapper;
import com.backend.mapper.StatisticsMapper;
import com.backend.pojo.ACServiceObject;
import com.backend.pojo.Statistics;
import com.backend.service.ManagerService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    @Qualifier("StatisticsMap")
    private ConcurrentHashMap<String, Statistics> statisticsMap;

    @Resource
    StatisticsMapper statisticsMapper;

    @Override
    public List<Statistics> getStatistics(String date) {
        List<Statistics> returnMap = statisticsMapper.getStatistics();
        // 先前所有的保单加上今天的报单
        returnMap.addAll(statisticsMap.values());
        return returnMap;
    }
    // 定时任务，由于使用了schedule注解，会自动提交到ServicingSchedulingConfig中，无法避免，反正我查到的避免方法都是要么这边别用schedule，要么那么别配定时任务线程池
    @Scheduled(fixedRate = 3600000) // 每小时执行一次
    public void scheduledTask() {
        int day = LocalDateTime.parse(statisticsMap.get("-1").getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).getDayOfMonth();
        int now_day = LocalDateTime.now().getDayOfMonth();
        if(now_day!=day){
            for (Statistics statistics:
                    statisticsMap.values()) {
                statisticsMapper.add(statistics);// 每个数据都写入数据库中
                // 第二天数据的初始化
                // 更新开始记录的日期，因为是一天的，所以不记录时分秒
                statistics.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                statistics.setDetailedBillSum(0);
                statistics.setDispatchSum(0);
                statistics.setRequestLength(0);
                statistics.setSpeedChangeSum(0);
                statistics.setSwitchSum(0);
                statistics.setTemChangeSum(0);
                statistics.setTotalFee(0);
            }
        }
    }
}
