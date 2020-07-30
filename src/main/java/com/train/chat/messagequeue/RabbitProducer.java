package com.train.chat.messagequeue;

import com.train.chat.pojo.Friend;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author MercerJR
 * @Data 2020/7/30 9:47
 */
@Component
public class RabbitProducer {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void deleteFriendMessage(String userId1,String userId2){
        Friend friend = new Friend(userId1,userId2,0);
        rabbitTemplate.convertAndSend("deleteFriendMessage",friend);
    }

    public void deleteRoomMessage(String roomId){
        rabbitTemplate.convertAndSend("deleteRoomMessage",roomId);
    }

}
