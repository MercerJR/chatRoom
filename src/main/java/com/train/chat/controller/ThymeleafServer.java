package com.train.chat.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;

/**
 * @Author MercerJR
 * @Data 2021/4/14 22:15
 */
@RestController
public class ThymeleafServer {
    @GetMapping("/")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping("/index")
    public ModelAndView index(String user, String password, HttpServletRequest request) throws UnknownHostException {
        if (StringUtils.isEmpty(user)) {
            user = "匿名用户";
        }
        ModelAndView mav = new ModelAndView("chat");
        mav.addObject("user", user);
//        mav.addObject("webSocketUrl", "ws://" + InetAddress.getLocalHost().getHostAddress() + ":" + request.getServerPort() + request.getContextPath() + "/hall");
        return mav;
    }
//    public ModelAndView index(){
//        return new ModelAndView("/chat");
//    }

}
