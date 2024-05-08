package com.backend.mapper;

import com.backend.pojo.Customer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CustomerMapper {
    @Insert("insert into customers values (#{customerId},#{contactNumber},#{customerName},#{customerGender})")
    void insertCustomer(Customer customer);

    @Select("select customerName from customers where customerId = #{customerId}")
    String selectNameById(String customerId);
}
