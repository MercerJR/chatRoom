package com.train.chat.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author MercerJR
 * @Data 2020/7/29 12:39
 */
@Configuration
public class RabbitConfigurator {

    @Bean
    public Queue deleteFriendMessageQueue() {
        return new Queue("deleteFriendMessage");
    }

    @Bean
    public Queue deleteRoomMessageQueue(){
        return new Queue("deleteRoomMessage");
    }

}
