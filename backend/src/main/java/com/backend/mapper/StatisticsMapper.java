package com.backend.mapper;

import com.backend.pojo.Statistics;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StatisticsMapper {
    @Insert("insert into statistics values(#{date},#{detailedBillSum},#{dispatchSum},#{requestLength},#{roomId},#{speedChangeSum},#{switchSum},#{temChangeSum},#{totalFee})")
    void add(Statistics statistics);

    @Select("select * from statistics where DATE between #{startDate} and #{endDate}")
    List<Statistics> getStatistics(String startDate,String endDate);
}
