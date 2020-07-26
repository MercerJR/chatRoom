package com.train.chat.service;

import com.train.chat.dao.ChatMessageMapper;
import com.train.chat.data.Message;
import com.train.chat.exception.CustomException;
import com.train.chat.exception.CustomExceptionType;
import com.train.chat.pojo.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author MercerJR
 * @Data 2020/7/26 10:17
 */
@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageMapper mapper;

    public void insertGroupMessage(String receiveId, String fromId, String message, Long time) {
        ChatMessage chatMessage = new ChatMessage(receiveId, fromId, message, time);
        if (!mapper.insert(chatMessage)) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, Message.CONTACT_ADMIN);
        }
    }

    public void insertFriendMessage(String receiveId, String fromId, String message, Long time) {
        ChatMessage chatMessage1 = new ChatMessage(receiveId, fromId, message, time);
        ChatMessage chatMessage2 = new ChatMessage(fromId, receiveId, message, time);
        if (!mapper.insert(chatMessage1) || !mapper.insert(chatMessage2)) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, Message.CONTACT_ADMIN);
        }
    }

    public List<String> getFriendMessage(String receiveId, String fromId) {
        return mapper.selectFriendMessage(receiveId,fromId);
    }

    public List<String> getGroupMessage(String receiveId) {
        return mapper.selectGroupMessage(receiveId);
    }
}
