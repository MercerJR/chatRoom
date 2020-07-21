package com.train.chat.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.train.chat.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * @Author MercerJR
 * @Data 2020/7/16 10:49
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InputMessage {

    private String message;

    private User[] list;

    /**
     * 0是普通信息，1是在线列表信息
     */
    private Integer type = 0;

    private Integer listNum = 0;

    /**
     * 信息指定发送的房间
     */
    private String target;

    public InputMessage(String message){
        this.message = message;
    }

    public InputMessage(String message,String target){
        this.message = message;
        this.target = target;
    }

    public InputMessage(){}

    public InputMessage(User[] list){
        this.list = list;
        this.type = 1;
        this.listNum = list.length;
    }

    public static InputMessage enterInfo(String username,int onlineNum) {
        String message = username + "加入了大厅，" + "当前在线" + onlineNum + "人";
        return new InputMessage(message,"大厅");
    }

    public static InputMessage leaveInfo(String username,int onlineNum){
        String message = username + "离开了大厅，" + "当前在线" + onlineNum + "人";
        return new InputMessage(message,"大厅");
    }

    public static InputMessage userOffLine(String userId){
        String message = "对方现在不在线";
        return new InputMessage(message,userId);
    }

    public static InputMessage publishMsg(String username,String msg,String target){
        String message = username + ":\n" + msg;
        return new InputMessage(message,target);
    }

    public static InputMessage publishMsg(String username,InputMessage inputMessage){
        String message = username + ":\n" + inputMessage.getMessage();
        inputMessage.setMessage(message);
        return inputMessage;
    }
}
