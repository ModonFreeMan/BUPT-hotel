package com.backend.config;

import com.backend.pojo.ACServiceObject;
import com.backend.pojo.CentralACStatus;
import com.backend.pojo.FiveRoomDetail;
import com.backend.pojo.Statistics;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
    @ConfigurationProperties(prefix = "rooms")
    public HashMap<String, FiveRoomDetail> initFiveRoomDetailsMap() {
        return new HashMap<>();
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
        for (int i = 100; i < 400; i += 100) {
            for (int j = 1; j < 11; j++) {
                // 初始化时只用设置房间号和日期
                String roomId = String.valueOf(i + j);
                Statistics theStatistics = new Statistics();
                theStatistics.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                theStatistics.setRoomId(roomId);
                StatisticsMap.put(roomId, theStatistics);
            }
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
