package com.train.chat.utils;

import com.train.chat.data.InputMessage;

import javax.websocket.DecodeException;
import javax.websocket.EndpointConfig;

/**
 * @Author MercerJR
 * @Data 2021/4/16 11:00
 */
public class MessageDecoder implements javax.websocket.Decoder.Text<InputMessage>  {
    @Override
    public InputMessage decode(String s) throws DecodeException {
        return null;
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
