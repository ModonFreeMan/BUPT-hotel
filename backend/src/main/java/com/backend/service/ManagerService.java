package com.backend.service;

import com.backend.pojo.Statistics;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ManagerService {
    List<Statistics> getStatistics(String date);
}
