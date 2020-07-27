package com.train.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.train.chat.configuration.HttpSessionConfigurator;
import com.train.chat.data.HttpInfo;
import com.train.chat.data.InputMessage;
import com.train.chat.pojo.User;
import com.train.chat.service.ChatMessageService;
import com.train.chat.service.RoomService;
import com.train.chat.utils.MessageDecoder;
import com.train.chat.utils.MessageEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author MercerJR
 * @Data 2020/7/17 8:37
 */
@Component
@Slf4j
@ServerEndpoint(value = "/hall", configurator = HttpSessionConfigurator.class,
        decoders = {MessageDecoder.class}, encoders = {MessageEncoder.class})
public class HallController {

    /**
     * 聊天房间以及每个房间里的用户会话信息，线程安全
     */
    private static Map<String, ConcurrentHashMap<Session, User>> rooms = new ConcurrentHashMap<>();

    private static Map<Session, User> users = new ConcurrentHashMap<>();

    private static Map<String, Session> sessions = new ConcurrentHashMap<>();

    private static Map<String, List<String>> addRooms = new ConcurrentHashMap<>();

    private Session session;

    private static RoomService roomService;

    private static ChatMessageService messageService;

    @Autowired
    public void setRoomService(RoomService service) {
        HallController.roomService = service;
    }

    @Autowired
    public void setMessageService(ChatMessageService service) {
        HallController.messageService = service;
    }

    static void putRooms(String roomId, User user) {
        Session session = sessions.get(user.getUserId());
        ConcurrentHashMap<Session, User> sessionMap = new ConcurrentHashMap<>();
        sessionMap.put(session, user);
        if (!rooms.containsKey(roomId)) {
            rooms.put(roomId, sessionMap);
        } else {
            rooms.get(roomId).put(session, user);
        }
        if (!addRooms.containsKey(user.getUserId())) {
            List<String> roomIdList = new ArrayList<>();
            roomIdList.add(roomId);
            addRooms.put(user.getUserId(), roomIdList);
        }
        addRooms.get(user.getUserId()).add(roomId);
    }

    static void exitRoom(String roomId, User user) {
        Session session = sessions.get(user.getUserId());
        rooms.get(roomId).remove(session);
        if (rooms.get(roomId).size() == 0) {
            rooms.remove(roomId);
        }
        addRooms.get(user.getUserId()).remove(roomId);
    }

    static void cancellation(User user) {
        Session session = sessions.get(user.getUserId());
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void deleteRoom(String roomId) {
        rooms.remove(roomId);
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        User user = (User) httpSession.getAttribute(HttpInfo.USER_SESSION);
        this.session = session;
        if (sessions.containsKey(user.getUserId())) {
            Session thatSession = sessions.get(user.getUserId());
            thatSession.close();
            users.remove(thatSession);
            sessions.put(user.getUserId(), session);
        }
        users.put(session, user);
        sessions.put(user.getUserId(), session);
        broadCast(InputMessage.enterInfo(user.getUsername()));
        showList();
        synchronized (this) {
            List<String> roomIdList = roomService.selectRoomIdByUser(user.getUserId());
            for (String roomId : roomIdList) {
                putRooms(roomId, user);
            }
        }
    }

    @OnMessage
    public void onMessage(String msg) {
        log.info("消息监控：" + msg);
        ObjectMapper objectMapper = new ObjectMapper();
        InputMessage inputMessage = null;
        try {
            inputMessage = objectMapper.readValue(msg, InputMessage.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String username = users.get(session).getUsername();
        assert inputMessage != null;
        if (inputMessage.getType().equals(InputMessage.infoUpdateList)){
            updateListInfo(inputMessage);
        }else {
            if (inputMessage.getType().equals(InputMessage.commonMessage)){
                InputMessage.publishMsg(username, inputMessage);
            }
            if (HttpInfo.HALL.equals(inputMessage.getTarget())) {
                broadCast(inputMessage);
            } else {
                if (inputMessage.getTarget().contains("R")) {
                    sendToTarget(inputMessage);
                } else {
                    sendToUser(inputMessage);
                }
            }
        }
    }

    @OnError
    public void onError(Throwable error) {
        log.info("发生错误！");
        error.printStackTrace();
    }

    @OnClose
    public void onClose() {
        User user = users.get(session);
        users.remove(session);
        sessions.remove(user.getUserId());
        for (String roomId : addRooms.get(user.getUserId())) {
            if (roomId != null){
                rooms.get(roomId).remove(session);
            }
        }
        addRooms.remove(user.getUserId());
        broadCast(InputMessage.leaveInfo(user.getUsername()));
        showList();
    }

    private void broadCast(InputMessage inputMessage) {
        for (Session session : users.keySet()) {
            try {
                session.getBasicRemote().sendObject(inputMessage);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendToTarget(InputMessage inputMessage) {
        String fromId = users.get(session).getUserId();
        String roomId = inputMessage.getTarget();
        if (rooms.get(roomId) != null){
            messageService.insertGroupMessage(inputMessage.getTarget(), fromId,
                    inputMessage.getMessage(), inputMessage.getTime());
            roomService.addAllMessageTag(inputMessage.getTarget());
            for (Session session : rooms.get(roomId).keySet()) {
                try {
                    session.getBasicRemote().sendObject(inputMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (EncodeException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendToUser(InputMessage inputMessage) {
        Session session = sessions.get(inputMessage.getTarget());
        String fromUser = users.get(this.session).getUserId();
        String targetUser = inputMessage.getTarget();
        messageService.insertFriendMessage(targetUser, fromUser,
                inputMessage.getMessage(), inputMessage.getTime());
        if (session != null) {
            try {
                this.session.getBasicRemote().sendObject(inputMessage);
                inputMessage.setTarget(fromUser);
                session.getBasicRemote().sendObject(inputMessage);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.session.getBasicRemote().sendObject(inputMessage);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateListInfo(InputMessage inputMessage){
        if (rooms.get(inputMessage.getTarget()) != null){
            for (Session session : rooms.get(inputMessage.getTarget()).keySet()){
                try {
                    session.getBasicRemote().sendObject(inputMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (EncodeException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendImageMessage(){

    }

    private void showList() {
        User[] list = new User[users.size()];
        int i = 0;
        for (User user : users.values()) {
            list[i] = user;
            i++;
        }
        for (Session session : users.keySet()) {
            try {
                session.getBasicRemote().sendObject(new InputMessage(list));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
                e.printStackTrace();
            }
        }
    }
}
