package com.train.chat.controller;

import com.train.chat.data.MessageListResponse;
import com.train.chat.data.Response;
import com.train.chat.data.UserListResponse;
import com.train.chat.pojo.ApplyMessage;
import com.train.chat.pojo.User;
import com.train.chat.service.FriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @Author MercerJR
 * @Data 2020/7/20 14:29
 */
@RestController
@Slf4j
@RequestMapping("/friend")
@Validated
public class FriendController {

    @Autowired
    private FriendService service;

    @PostMapping(value = "/addFriend",produces = "application/json")
    public Response addFriend(@RequestBody String friendId, HttpSession session){
        log.info(friendId);
        User user = (User) session.getAttribute("user");
        service.addFriend(user.getUserId(),friendId);
        return new Response().success();
    }

    @PostMapping(value = "/agreeApply",produces = "application/json")
    public Response agreeApply(@RequestBody ApplyMessage applyMessage, HttpSession session){
        User user = (User) session.getAttribute("user");
        service.agreeApply(user,applyMessage);
        return new Response().success();
    }

    @PostMapping(value = "/refuseApply",produces = "application/json")
    public Response refuseApply(@RequestBody ApplyMessage applyMessage,HttpSession session){
        User user = (User) session.getAttribute("user");
        service.refuseApply(user.getUserId(),applyMessage);
        return new Response().success();
    }

    @GetMapping(value = "/displayMessage",produces = "application/json")
    public Response displayMessage(HttpSession session){
        User user = (User) session.getAttribute("user");
        return new Response().success(new MessageListResponse(service.displayMessageList(user.getUserId())));
    }

    @GetMapping(value = "/displayFriends",produces = "application/json")
    public Response displayFriends(HttpSession session){
        User user = (User) session.getAttribute("user");
        return new Response().success(new UserListResponse(service.displayFriends(user.getUserId())));
    }

}
