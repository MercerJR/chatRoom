package com.train.chat.data;

import com.train.chat.pojo.UserInfo;
import lombok.Data;

/**
 * @Author MercerJR
 * @Data 2020/7/28 15:16
 */
@Data
public class UserInfoResponse {
    private UserInfo userInfo;
    private boolean friend;

    public UserInfoResponse(UserInfo userInfo,boolean isFriend){
        this.userInfo = userInfo;
        this.friend = isFriend;
    }
}
