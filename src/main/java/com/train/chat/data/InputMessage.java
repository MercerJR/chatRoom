package com.train.chat.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.train.chat.pojo.User;
import com.train.chat.utils.DateFormatUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

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
     * 0是普通信息，1是在线列表信息，2是文件信息
     */
    private Integer type = 0;

    private Integer listNum = 0;

    /**
     * 信息指定发送的房间或用户
     */
    private String target;

    private Long time;

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

    public static InputMessage enterInfo(String username) {
        String message = username + "加入了大厅" ;
        return new InputMessage(message,HttpInfo.HALL);
    }

    public static InputMessage leaveInfo(String username){
        String message = username + "离开了大厅" ;
        return new InputMessage(message,HttpInfo.HALL);
    }

    public static InputMessage userOffLine(String userId){
        String message = "对方现在不在线";
        return new InputMessage(message,userId);
    }

    public static InputMessage publishMsg(String username,InputMessage inputMessage){
        Long time = System.currentTimeMillis();
        String date = DateFormatUtil.getDateByMiles(time);
        String message = date + "  " + username + ":\n" + inputMessage.getMessage();
        inputMessage.setMessage(message);
        inputMessage.setTime(time);
        return inputMessage;
    }
}
