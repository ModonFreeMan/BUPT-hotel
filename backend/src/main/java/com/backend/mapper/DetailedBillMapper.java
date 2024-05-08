package com.backend.mapper;

import com.backend.pojo.DetailedBill;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author seaside
 * 2024-05-08 17:50
 */
@Mapper
public interface DetailedBillMapper {
    @Insert("insert into detailedbills values(#{serviceId},#{endTem},#{fee}),#{feeRate},#{roomId},#{speedLevel}),#{startTem},#{startTime}")
    void insertBill(DetailedBill detailedBill, String serviceId);

    @Select("select * from detailedbills where serviceId = #{serviceId}")
    DetailedBill getDetailBillByServiceId(String serviceId);

}
