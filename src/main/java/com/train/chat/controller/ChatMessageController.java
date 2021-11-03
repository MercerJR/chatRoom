package com.train.chat.controller;

import com.train.chat.data.ChatMessageResponse;
import com.train.chat.data.HttpInfo;
import com.train.chat.data.Response;
import com.train.chat.pojo.User;
import com.train.chat.service.ChatMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author MercerJR
 * @Data 2021/4/26 11:59
 */
@RestController
@Slf4j
@RequestMapping("/chatMessage")
@Validated
public class ChatMessageController {

    @Autowired
    private ChatMessageService service;

    @GetMapping(value = "/getFriendMessage/{fromId}", produces = "application/json")
    public Response getFriendMessage(HttpSession session, @PathVariable("fromId") String fromId) {
        User user = (User) session.getAttribute(HttpInfo.USER_SESSION);
        List<String> chatMessageList = service.getFriendMessage(user.getUserId(), fromId);
        return new Response().success(new ChatMessageResponse(chatMessageList));
    }

    @GetMapping(value = "/getGroupMessage/{receiveId}", produces = "application/json")
    public Response getGroupMessage(@PathVariable("receiveId") String receiveId) {
        List<String> chatMessageList = service.getGroupMessage(receiveId);
        return new Response().success(new ChatMessageResponse(chatMessageList));
    }
}
