package com.backend.service.Impl;

import com.backend.mapper.StatisticsMapper;
import com.backend.pojo.Statistics;
import com.backend.service.ManagerService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public List<Statistics> getStatistics(String startDate, String endDate) {
        List<Statistics> returnMap = statisticsMapper.getStatistics(startDate, endDate);
        // 先前所有的数据加上处于今天的房间的数据
        for (Statistics statistics:statisticsMap.values()) {
            if(dateComparison(startDate,endDate,statistics.getDate())){
                returnMap.add(statistics);
            }
        }
        return returnMap;
    }
    public boolean dateComparison(String startDate, String endDate,String checkDate){
        try {
            Date startDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            Date checkDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(checkDate);
            Date endDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            // 大于起始时间，小于截止时间，已经等于这二者的情况返回true
            return (checkDate1.after(startDate1) && checkDate1.before(endDate1))||startDate.equals(checkDate)||endDate.equals(checkDate);
        } catch (ParseException e) {
            System.out.println("Wrong date format"+e.getMessage());
        }
        return false;
    }
}
