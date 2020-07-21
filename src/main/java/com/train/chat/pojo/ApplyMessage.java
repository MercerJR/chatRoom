package com.train.chat.pojo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author MercerJR
 * @Data 2020/7/21 18:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyMessage implements Serializable {
    private String msgId;

    private String userId;

    private String applyId;

    private String message;

    private Integer type;

    private static final long serialVersionUID = 1L;
}