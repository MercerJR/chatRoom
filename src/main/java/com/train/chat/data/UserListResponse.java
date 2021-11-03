package com.train.chat.data;

import com.train.chat.pojo.User;
import lombok.Data;

import java.util.List;

/**
 * @Author MercerJR
 * @Data 2021/4/17 15:23
 */
@Data
public class UserListResponse {
    private List<User> userList;
    private Integer size;

    public UserListResponse(List<User> userList){
        this.userList = userList;
        this.size = userList.size();
    }
}
