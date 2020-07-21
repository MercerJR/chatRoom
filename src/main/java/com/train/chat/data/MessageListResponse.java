package com.train.chat.data;

import com.train.chat.pojo.ApplyMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Author MercerJR
 * @Data 2020/7/21 10:39
 */
@Data
@AllArgsConstructor
public class MessageListResponse {
    List<ApplyMessage> applyMessageList;
    Integer size;

    public MessageListResponse(List<ApplyMessage> applyMessageList){
        this.applyMessageList = applyMessageList;
        this.size = applyMessageList.size();
    }
}
