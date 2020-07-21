package com.train.chat.pojo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author MercerJR
 * @Data 2020/7/16 21:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAndRoom implements Serializable {
    private String userId;

    private String roomId;

    private static final long serialVersionUID = 1L;
}