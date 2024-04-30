package com.backend.service.Impl;

import com.backend.pojo.Statistics;
import com.backend.service.ManagerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerServiceImpl implements ManagerService {
    @Override
    public List<Statistics> getStatistics(String date) {
        return null;
    }
}
