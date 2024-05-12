package com.backend.service;

import com.backend.pojo.Statistics;

import java.util.List;

public interface ManagerService {
    List<Statistics> getStatistics(String startDate, String endDate);
}
