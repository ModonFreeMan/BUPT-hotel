package com.backend.config;

import com.backend.pojo.ACServiceObject;
import com.backend.pojo.CentralACStatus;
import com.backend.pojo.FiveRoomDetail;
import com.backend.pojo.Statistics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Configuration
public class GlobalConfiguration {

    @Bean(name = "FiveRoomDetailsMap")
    public HashMap<String, FiveRoomDetail> initFiveRoomDetailsMap() {
        HashMap<String, FiveRoomDetail> maps = new HashMap<>(); //只读的map
        maps.put("1", new FiveRoomDetail(32, 100)); //todo：房间Id待确定
        maps.put("2", new FiveRoomDetail(28, 125));
        maps.put("3", new FiveRoomDetail(30, 150));
        maps.put("4", new FiveRoomDetail(29, 200));
        maps.put("5", new FiveRoomDetail(35, 100));
        return maps;
    }

    @Bean(name = "ACServiceMap")
    public ConcurrentHashMap<String, ACServiceObject> initACServiceMap() {
        // 在这里初始化空调服务对象列表
        return new ConcurrentHashMap<>();
    }

    @Bean(name = "CentralACStatus")
    public CentralACStatus initCentralACStatus() {
        // 在这里初始化中央空调对象
        CentralACStatus centralACStatus = new CentralACStatus();
        centralACStatus.setSwitchStatus(true);
        centralACStatus.setLowerBound(18);
        centralACStatus.setUpperBound(28);
        centralACStatus.setWorkMode(false);
        centralACStatus.setRate(1d);
        return centralACStatus;
    }

    @Bean(name = "StatisticsMap")
    public ConcurrentHashMap<String, Statistics> initStatisticsMap() {
        // 在这里初始化阶段报表hashmap对象
        ConcurrentHashMap<String, Statistics> StatisticsMap = new ConcurrentHashMap<>();
        for (int i = 1; i < 6; i += 1) {// 只有一到五几个房间
            // 初始化时只用设置房间号和日期
            String roomId = String.valueOf(i);
            Statistics theStatistics = new Statistics();
            theStatistics.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            theStatistics.setRoomId(roomId);
            StatisticsMap.put(roomId, theStatistics);
        }
        Statistics theStatistics = new Statistics();
        theStatistics.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        theStatistics.setRoomId("-1");
        StatisticsMap.put("-1", theStatistics);
        return StatisticsMap;
    }

    @Bean(name = "RecoveryQueue")
    public CopyOnWriteArrayList<String> initRecoveryQueue() {
        return new CopyOnWriteArrayList<>();
    }


}
