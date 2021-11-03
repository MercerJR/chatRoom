package com.train.chat.messagequeue;

import com.train.chat.dao.ChatMessageMapper;
import com.train.chat.dao.RoomMapper;
import com.train.chat.dao.UserAndRoomMapper;
import com.train.chat.data.HttpInfo;
import com.train.chat.data.Message;
import com.train.chat.exception.CustomException;
import com.train.chat.exception.CustomExceptionType;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author MercerJR
 * @Data 2021/4/30 10:23
 */
@Component
@RabbitListener(queues = "deleteRoomMessage")
public class DeleteRoomMsgConsumer {

    @Autowired
    private UserAndRoomMapper mapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @RabbitHandler
    public void deleteRoomMessage(String roomId){
        String membersKey = roomId + ":" + HttpInfo.REDIS_MEMBERS_SUFFIX;
        if (mapper.selectMembersNum(roomId) == 0){
            if (!roomMapper.deleteByPrimaryKey(roomId)){
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR, Message.CONTACT_ADMIN);
            }
            redisTemplate.delete(membersKey);
            chatMessageMapper.deleteMessageByRoom(roomId);
        }
    }
}
