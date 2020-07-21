package com.train.chat.controller;

import com.train.chat.pojo.User;
import com.train.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author MercerJR
 * @Data 2020/7/15 22:40
 */
@Controller
public class IndexController {

//    @Autowired
//    private UserService service;
//
//    @GetMapping("/")
//    public String index(HttpServletRequest request) {
//
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null && cookies.length != 0) {
//            for (Cookie cookie : cookies) {
//                if ("userId".equals(cookie.getName())) {
//                    String userId = cookie.getValue();
//                    User user = service.getUserById(userId);
//
//                    if (user != null) {
//                        request.getSession().setAttribute("user", user);
//                    }
//                    break;
//                }
//            }
//        }
//        return "chat";
//    }

}
