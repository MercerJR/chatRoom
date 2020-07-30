package com.train.chat.dao;

import com.train.chat.pojo.ChatMessage;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @Author MercerJR
 * @Data 2020/7/26 12:23
 */
public interface ChatMessageMapper {
    boolean insert(ChatMessage record);

    boolean insertSelective(ChatMessage record);

    List<String> selectFriendMessage(@Param("receiveId") String receiveId, @Param("fromId") String fromId);

    List<String> selectGroupMessage(String receiveId);

    void deleteMessageByRoom(String roomId);

    boolean deleteMessageByFriends(@Param("receiveId") String receiveId, @Param("fromId") String fromId);

    ChatMessage getFriendMessage(@Param("receiveId") String receiveId, @Param("fromId") String fromId);
}