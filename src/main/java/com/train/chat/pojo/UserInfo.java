package com.train.chat.pojo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;


/**
 * @Author MercerJR
 * @Data 2020/7/20 17:03
 */
@Data
@AllArgsConstructor
public class UserInfo implements Serializable {

    private String userId;

    @Size(min = 4,max = 15,message = "用户名的长度应在4到15个字符以内")
    private String username;

    @Size(max = 2,message = "性别建议输入男或女，不要输入过多其他字符")
    private String gender = "";

    @Digits(integer = 3,fraction = 0,message = "年龄请不能输入超过三个数字")
    private Integer age = 0;

    @Size(max = 50,message = "爱好不要输入超过50个字符")
    private String hobby = "";

    @Size(max = 50,message = "家乡不要输入超过50个字符")
    private String hometown = "";

    @Size(max = 50,message = "个人描述不要输入超过50个字符")
    private String describe = "";

    private static final long serialVersionUID = 1L;

    public UserInfo(String userId,String username){
        this.userId = userId;
        this.username = username;
    }

    public UserInfo(){}
}