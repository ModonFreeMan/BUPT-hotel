package com.backend.mapper;


import com.backend.pojo.TotalBill;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


import java.util.List;

@Mapper
public interface TotalBillMapper {
    @Select("select * from total_bills where customerId = #{customerId}")
    List<TotalBill> getTotalBill(String customerId);

    @Select("select * from total_bills where serviceId = #{serviceId}")
    TotalBill getTotalBillByServiceId(String serviceId);
    @Insert("insert into total_bills values(#{serviceId},#{customerId},#{customerGender},#{customerName},#{contactNumber})")
    void InitialBill(TotalBill totalBill);

}
