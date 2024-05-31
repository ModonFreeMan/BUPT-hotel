package com.backend.service;

import com.backend.pojo.Statistics;

import java.util.List;
/**
 * 程序标题：ManagerService
 * 模块描述：这个接口定义了管理服务的方法，包括获取统计信息等功能。
 * 接口说明：这个接口的实现类可以被其他模块调用。
 * 负责人：陈俞涵 创建日期：2021/4/30
 */
public interface ManagerService {
    /**
     * 程序标题：getStatistics
     * 模块描述：这个方法返回给定日期范围内的统计信息。
     * 接口说明：这个方法可以被其他模块调用，以获取统计信息。
     * 负责人：陈俞涵
     */
    List<Statistics> getStatistics(String startDate, String endDate);
}
