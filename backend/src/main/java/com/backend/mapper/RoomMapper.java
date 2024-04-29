package com.backend.mapper;

import com.backend.pojo.Room;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface RoomMapper {

    @Insert("insert into Rooms values(#{roomId},#{content},#{checkinDate},#{customerId},#{customerGender},#{checkinStatus})")
    void add(Room room);


    @Select("select * from Rooms where roomId = #{roomId}")
    Room getRoom(String roomId);

    @Update("update Rooms " +
            "set checkinDate = #{checkinDate},customerId=#{customerId},customerGender=#{customerGender},checkinStatus = #{checkinStatus} " +
            "where roomId = #{roomId}" )
    void updateRoom(Room room);

    List<Room> roomList(String roomId);
}