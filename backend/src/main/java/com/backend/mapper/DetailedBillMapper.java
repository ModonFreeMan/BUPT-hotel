package com.backend.mapper;

import com.backend.pojo.DetailedBill;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author seaside
 * 2024-05-08 17:50
 */
@Mapper
public interface DetailedBillMapper {
    @Select("select * from detailedbills where serviceId = #{serviceId}")
    List<DetailedBill> getDetailedBills(String serviceId);

    @Insert("insert into detailedbills values(#{serviceId},#{endTem},#{fee},#{rate},#{roomId},#{speedLevel},#{startTem},#{startTime}, #{requestTime},#{serviceLength})")
    void insertBill(String serviceId, double endTem, String endTime, double fee, double rate, String roomId, int speedLevel, double startTem, String startTime, String requestTime, String serviceLength);


}
