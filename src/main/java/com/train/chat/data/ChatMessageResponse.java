package com.train.chat.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author MercerJR
 * @Data 2021/4/26 12:30
 */
@Data
@AllArgsConstructor
public class ChatMessageResponse {

    private List<String> chatMessageList;

    private Integer size;

    public ChatMessageResponse(List<String> chatMessageList){
        this.chatMessageList = chatMessageList;
        this.size = chatMessageList.size();
    }

}
