package com.train.chat.dao;

import com.train.chat.pojo.User;
import com.train.chat.pojo.UserAndRoom;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author MercerJR
 * @Data 2020/7/16 21:15
 */
public interface UserAndRoomMapper {
    boolean insert(UserAndRoom record);

    boolean insertSelective(UserAndRoom record);

    List<User> selectUserByRoom(String roomId);

    UserAndRoom selectRecord(@Param("userId") String userId, @Param("roomId") String roomId);

    List<String> selectRoomIdByUser(String userId);
}