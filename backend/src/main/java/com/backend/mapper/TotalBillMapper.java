package com.backend.mapper;


import com.backend.pojo.TotalBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TotalBillMapper {
    @Select("select * from total_bills where customerId = #{customerId}")
    TotalBill getTotalBill(String customerId);
}
