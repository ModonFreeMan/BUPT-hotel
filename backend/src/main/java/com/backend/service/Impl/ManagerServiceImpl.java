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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    @Qualifier("StatisticsMap")
    private ConcurrentHashMap<String, Statistics> statisticsMap;

    @Resource
    StatisticsMapper statisticsMapper;

    private static Statistics mergeStatistics(Statistics left, Statistics right) {
        right.setDetailedBillSum(left.getDetailedBillSum() + right.getDetailedBillSum());
        right.setDispatchSum(left.getDispatchSum() + right.getDispatchSum());
        right.setRequestLength(left.getRequestLength() + right.getRequestLength());
        right.setSpeedChangeSum(left.getSpeedChangeSum() + right.getSpeedChangeSum());
        right.setSwitchSum(left.getSwitchSum() + right.getSwitchSum());
        right.setTemChangeSum(left.getTemChangeSum() + right.getTemChangeSum());
        right.setTotalFee(left.getTotalFee() + right.getTotalFee());
        return right;
    }

    @Override
    public List<Statistics> getStatistics(String startDate, String endDate) {
        List<Statistics> returnList = statisticsMapper.getStatistics(startDate, endDate);
        // 先前所有的数据加上处于今天的房间的数据(复制版)
        for (Statistics statistics:statisticsMap.values()) {
            if(dateComparison(startDate,endDate,statistics.getDate())){
                Statistics copy_statistics = new Statistics();

                copy_statistics.setTotalFee(statistics.getTotalFee());
                copy_statistics.setDetailedBillSum(statistics.getDetailedBillSum());
                copy_statistics.setDispatchSum(statistics.getDispatchSum());
                copy_statistics.setRequestLength(statistics.getRequestLength());
                copy_statistics.setSpeedChangeSum(statistics.getSpeedChangeSum());
                copy_statistics.setSwitchSum(statistics.getSwitchSum());
                copy_statistics.setTemChangeSum(statistics.getTemChangeSum());
                copy_statistics.setRoomId(statistics.getRoomId());
                copy_statistics.setDate(statistics.getDate());

                returnList.add(copy_statistics);
            }
        }
        // 统计数据
        returnList = new ArrayList<>( returnList.stream().collect(Collectors.groupingBy(Statistics::getRoomId,
                Collectors.reducing(new Statistics(),
                        ManagerServiceImpl::mergeStatistics
                        )
                )).values());
        // 将所有数据合并到一个statistics中
        Statistics total_statistic = returnList.stream().reduce(new Statistics(), ManagerServiceImpl::mergeStatistics);
        total_statistic.setRoomId("-1");
        total_statistic.setDate(endDate);
        returnList.add(total_statistic);
        for(Statistics statistics:returnList){
            statistics.setTotalFee(Double.parseDouble(String.format("%.2f",statistics.getTotalFee())));
        }
        return returnList;
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
