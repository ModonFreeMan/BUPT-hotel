package com.backend.mapper;

import com.backend.pojo.Room;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface RoomMapper {

    @Insert("insert into rooms values(#{roomId},#{content},#{checkinDate},#{customerId},#{customerGender},#{checkinStatus})")
    void add(Room room);


    @Select("select * from Rooms where roomId = #{roomId}")
    Room getRoom(String roomId);

    @Update("update Rooms " +
            "set customerId=#{customerId},customerGender=#{customerGender},checkinStatus = #{checkinStatus} " +
            "where roomId = #{roomId}" )
    void updateRoom(Room room);
    //应该不需要checkinDate = #{checkinDate}，修改记录时数据库会自己修改该时间戳

    @Select("select * from Rooms")
    List<Room> roomList();

    //将某个房间状态设为false
    @Update("update Rooms set checkinStatus = false where roomId = #{roomId}")
    void setRoomFree(String roomId);
}