package com.train.chat.pojo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author MercerJR
 * @Data 2021/4/27 0:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room implements Serializable {
    private String roomId;

    private String roomName;

    private static final long serialVersionUID = 1L;
}