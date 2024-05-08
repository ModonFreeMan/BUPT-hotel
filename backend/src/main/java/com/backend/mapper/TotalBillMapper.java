package com.backend.mapper;


import com.backend.pojo.TotalBill;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


import java.util.List;

@Mapper
public interface TotalBillMapper {
    @Select("select * from totalbills where customerId = #{customerId}")
    List<TotalBill> getTotalBill(String customerId);

    @Select("select * from totalbills where serviceId = #{serviceId}")
    TotalBill getTotalBillByServiceId(String serviceId);
    @Insert("insert into totalbills values(#{serviceId},#{customerId},#{customerName})")
    void InitialBill(TotalBill totalBill);

    @Insert("insert into totalbills values(#{serviceId},#{customerId},#{customerName}," +
            "#{totalFee},#{acFee},#{days},#{roomFee},#{roomId},#{roomType})")
    void insertBill(TotalBill totalBill);
}
