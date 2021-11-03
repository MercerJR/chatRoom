package com.train.chat.pojo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author MercerJR
 * @Data 2021/4/20 14:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friend implements Serializable {
    private String user1;

    private String user2;

    private Integer tag;

    private static final long serialVersionUID = 1L;
}