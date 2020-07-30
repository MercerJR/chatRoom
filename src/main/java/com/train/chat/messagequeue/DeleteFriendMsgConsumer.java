package com.train.chat.messagequeue;

import com.train.chat.dao.ChatMessageMapper;
import com.train.chat.data.Message;
import com.train.chat.exception.CustomException;
import com.train.chat.exception.CustomExceptionType;
import com.train.chat.pojo.Friend;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author MercerJR
 * @Data 2020/7/30 9:53
 */
@Component
@RabbitListener(queues = "deleteFriendMessage")
public class DeleteFriendMsgConsumer {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @RabbitHandler
    public void deleteChatMessage(Friend friend){
        String userId1 = friend.getUser1();
        String userId2 = friend.getUser2();
        if (chatMessageMapper.getFriendMessage(userId1,userId2) != null){
            if (!chatMessageMapper.deleteMessageByFriends(userId1,userId2)
                    || !chatMessageMapper.deleteMessageByFriends(userId2,userId1)){
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR, Message.CONTACT_ADMIN);
            }
        }
    }
}
