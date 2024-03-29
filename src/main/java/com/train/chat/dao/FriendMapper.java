package com.train.chat.dao;

import com.train.chat.pojo.Friend;
import com.train.chat.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author MercerJR
 * @Data 2021/4/20 14:35
 */
public interface FriendMapper {
    boolean insert(Friend record);

    boolean insertSelective(Friend record);

    Friend selectFriendRecord(@Param("user1") String userId, @Param("user2") String friendId);

    boolean updateTag(@Param("user1")String applyId, @Param("user2")String userId);

    boolean deleteTag(@Param("user1")String applyId, @Param("user2")String userId);

    List<User> selectFriendsByUser(String userId);

    boolean deleteRecord(@Param("user1") String userId, @Param("user2") String friendId);
}