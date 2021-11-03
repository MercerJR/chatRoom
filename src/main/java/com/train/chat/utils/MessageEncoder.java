package com.train.chat.utils;

import com.alibaba.fastjson.JSON;
import com.train.chat.data.InputMessage;

import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;

/**
 * @Author MercerJR
 * @Data 2021/4/16 11:01
 */
public class MessageEncoder implements javax.websocket.Encoder.Text<InputMessage> {
    @Override
    public String encode(InputMessage inputMessage) throws EncodeException {
        return JSON.toJSONString(inputMessage);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
