package com.train.chat.pojo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author MercerJR
 * @Data 2021/4/26 14:54
 */
@Data
@AllArgsConstructor
public class UserAndRoom implements Serializable {
    private String userId;

    private String roomId;

    private Integer messageTag = 0;

    private static final long serialVersionUID = 1L;

    public UserAndRoom(String userId,String roomId){
        this.userId = userId;
        this.roomId = roomId;
    }

}