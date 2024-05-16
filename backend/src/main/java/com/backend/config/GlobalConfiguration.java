package com.backend.config;

import com.backend.pojo.ACServiceObject;
import com.backend.pojo.CentralACStatus;
import com.backend.pojo.FiveRoomDetail;
import com.backend.pojo.Statistics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Configuration
@PropertySource("classpath:application.yml")
public class GlobalConfiguration {

    @Value("${roomSum.number}")
    private int roomSum;

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
        for (int i = 1; i <= roomSum; i += 1) {// 只有一到五几个房间
            // 初始化时只用设置房间号和日期
            String roomId = "10"+ i;
            Statistics theStatistics = new Statistics();
            theStatistics.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            theStatistics.setRoomId(roomId);
            StatisticsMap.put(roomId, theStatistics);
        }
        return StatisticsMap;
    }

    @Bean(name = "RecoveryQueue")
    public CopyOnWriteArrayList<String> initRecoveryQueue() {
        return new CopyOnWriteArrayList<>();
    }

    @Bean
    public CorsFilter corsFilter() {
        //1. 添加 CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //放行哪些原始域
        config.addAllowedOrigin("*");
//        //是否发送 Cookie
//        config.setAllowCredentials(true);
        //放行哪些请求方式
        config.addAllowedMethod("*");
        //放行哪些原始请求头部信息
        config.addAllowedHeader("*");
        //暴露哪些头部信息
        config.addExposedHeader("*");
        //2. 添加映射路径
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**",config);
        //3. 返回新的CorsFilter
        return new CorsFilter(corsConfigurationSource);
    }

}
