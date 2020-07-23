package com.train.chat.controller;

import com.train.chat.data.Message;
import com.train.chat.data.Response;
import com.train.chat.data.UserListResponse;
import com.train.chat.exception.CustomException;
import com.train.chat.exception.CustomExceptionType;
import com.train.chat.pojo.User;
import com.train.chat.pojo.UserInfo;
import com.train.chat.service.UserService;
import com.train.chat.utils.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author MercerJR
 * @Data 2020/7/15 9:12
 */
@RestController
@Slf4j
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private IdUtil idUtil;

    @PostMapping(value = "/register", produces = "application/json")
    public Response register(@RequestBody User user) {
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            throw new CustomException(CustomExceptionType.VALIDATE_ERROR, Message.NAME_PASS_EMPTY);
        }
        if (service.existUsername(user.getUsername())){
            throw new CustomException(CustomExceptionType.VALIDATE_ERROR,Message.USERNAME_BE_USED);
        }
        user.setUserId("U" + idUtil.getPrimaryKey());
        service.register(user);
        return new Response().success();
    }

    @PostMapping(value = "/login", produces = "application/json")
    public Response login(@RequestBody User user, HttpSession session) {
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            throw new CustomException(CustomExceptionType.VALIDATE_ERROR, Message.NAME_PASS_EMPTY);
        }
        User loginUser = service.login(user.getUsername(), user.getPassword());
        if (loginUser != null) {
            session.setAttribute("user", loginUser);
            return new Response().success(loginUser.getUserId());
        } else {
            throw new CustomException(CustomExceptionType.VALIDATE_ERROR, Message.NAME_PASS_ERROR);
        }
    }

    @PostMapping(value = "/cancellation", produces = "application/json")
    public Response cancellation(HttpSession session) {
        if (session != null) {
            session.invalidate();

        }
        return new Response().success();
    }

    @GetMapping(value = "/selectUser/{userInfo}", produces = "application/json")
    public Response selectUser(@PathVariable("userInfo") String userInfo) {
        List<User> userList = service.selectUserByInfo(userInfo);
        return new Response().success(new UserListResponse(userList));
    }

    @PutMapping(value = "/updateInfo",produces = "application/json")
    public Response updateInfo(@RequestBody UserInfo userInfo,HttpSession session){
        User user = (User) session.getAttribute("user");
        userInfo.setUserId(user.getUserId());
        service.updateUserInfo(userInfo,user.getUsername());
        return new Response().success();
    }

    @GetMapping(value = "/displayUserInfo",produces = "application/json")
    public Response displayUserInfo(HttpSession session){
        User user = (User) session.getAttribute("user");
        UserInfo userInfo = service.displayUserInfo(user.getUserId());
        return new Response().success(userInfo);
    }

    @GetMapping(value = "/selectUserInfo/{userId}",produces = "application/json")
    public Response displayUserInfo(@PathVariable("userId")String userId){
        UserInfo userInfo = service.displayUserInfo(userId);
        return new Response().success(userInfo);
    }
}
