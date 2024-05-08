package com.backend.service.Impl;

import com.backend.mapper.StatisticsMapper;
import com.backend.pojo.Statistics;
import com.backend.service.ManagerService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
        // 先前所有的数据加上今天的数据
        returnMap.addAll(statisticsMap.values());
        return returnMap;
    }
}
