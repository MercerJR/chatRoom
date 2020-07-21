package com.train.chat.pojo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author MercerJR
 * @Data 2020/7/20 17:03
 */
@Data
@AllArgsConstructor
public class UserInfo implements Serializable {
    private String userId;

    private String username;

    private String gender = "";

    private Integer age = 0;

    private String hobby = "";

    private String hometown = "";

    private String describe = "";

    private static final long serialVersionUID = 1L;

    public UserInfo(String userId,String username){
        this.userId = userId;
        this.username = username;
    }

    public UserInfo(){}
}