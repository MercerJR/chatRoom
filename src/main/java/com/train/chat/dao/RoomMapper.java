package com.train.chat.dao;

import com.train.chat.pojo.Room;

import java.util.List;

/**
 * @Author MercerJR
 * @Data 2020/7/16 21:16
 */
public interface RoomMapper {
    boolean deleteByPrimaryKey(String roomId);

    boolean insert(Room record);

    boolean insertSelective(Room record);

    Room selectByPrimaryKey(String roomId);

    boolean updateByPrimaryKeySelective(Room record);

    boolean updateByPrimaryKey(Room record);

    Room existRoom(String room);

    List<Room> selectRoomByUser(String userId);

    List<Room> selectRoomByInfo(String roomInfo);
}