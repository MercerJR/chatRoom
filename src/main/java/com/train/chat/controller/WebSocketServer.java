package com.train.chat.controller;

import com.train.chat.configuration.HttpSessionConfigurator;
import com.train.chat.data.InputMessage;
import com.train.chat.pojo.User;
import com.train.chat.service.RoomService;
import com.train.chat.utils.IdUtil;
import com.train.chat.utils.MessageDecoder;
import com.train.chat.utils.MessageEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author MercerJR
 * @Data 2020/7/14 22:08
 */
@Component
@ServerEndpoint(value = "/chat/{target}", configurator = HttpSessionConfigurator.class,
        decoders = {MessageDecoder.class}, encoders = {MessageEncoder.class})
public class WebSocketServer {
    /**
     * 当前总的在线人数
     */
    private static Integer onlineNum = 0;
    /**
     * 聊天房间以及每个房间里的用户会话信息，线程安全
     */
    private static Map<String, ConcurrentHashMap<Session, User>> rooms = new ConcurrentHashMap<>();

    private static Map<String, Session> users = new ConcurrentHashMap<>();

    private Session session;

    private static RoomService service;

    private static IdUtil idUtil;

    @Autowired
    public void setRoomService(RoomService service) {
        WebSocketServer.service = service;
    }

    @Autowired
    public void setIdUtil(IdUtil idUtil) {
        WebSocketServer.idUtil = idUtil;
    }

    /**
     * 连接成功
     *
     * @param session 会话信息
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("target") String target, EndpointConfig config) throws IOException {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        User user = (User) httpSession.getAttribute("user");
        this.session = session;
        if (users.containsKey(user.getUserId())) {
            Session thatSession = users.get(user.getUserId());
            thatSession.close();
            users.put(user.getUserId(), session);
        }
        users.put(user.getUserId(), session);
        ConcurrentHashMap<Session, User> sessionMap = new ConcurrentHashMap<>();
        sessionMap.put(session, user);
        if (!service.existRoom(target)) {
            String roomId = "R" + idUtil.getPrimaryKey();
//            service.enterRoom(user.getUserId(), roomId, target);
            rooms.put(target, sessionMap);
        } else {
            if (!rooms.containsKey(target)) {
                rooms.put(target, sessionMap);
            } else {
                rooms.get(target).put(session, user);
            }
        }
        information(target, user.getUsername() + "加入了当前房间," + target + "在线 " + rooms.get(target).size() + " 人");
//        showList(target);
        this.addOnlineNum();
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose(@PathParam("target") String target) {
        User user = rooms.get(target).get(session);
        rooms.get(target).remove(session);
        users.remove(user.getUserId());
        int onlineNum = rooms.get(target).size();
        if (onlineNum > 0) {
            information(target, user.getUsername() + "离开了当前房间," + target + "在线 " + rooms.get(target).size() + " 人");
//            showList(target);
        }
        if (onlineNum == 0) {
            rooms.remove(target);
        }
        this.subOnlineNum();
    }

    /**
     * 连接错误
     *
     * @param error
     */
    @OnError
    public void onError(Throwable error) {
        System.err.println("发生错误！");
        error.printStackTrace();
    }

    /**
     * 收到客户端消息回调方法
     *
     * @param msg
     */
    @OnMessage
    public void onMessage(@PathParam("target") String target, String msg) {
        System.out.println("消息监控：" + msg);
        broadCast(target, msg);
    }

    private void broadCast(String target, String msg) {
        String username = rooms.get(target).get(session).getUsername();
        String message = username + ":\n" + msg + "\n";
        for (Session session : rooms.get(target).keySet()) {
            try {
                session.getBasicRemote().sendObject(new InputMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            } catch (EncodeException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    private void information(String target, String msg) {
        for (Session session : rooms.get(target).keySet()) {
            try {
                session.getBasicRemote().sendObject(new InputMessage(msg));
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            } catch (EncodeException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    private void sendAll(String msg) {
        for (Session session : users.values()) {
            try {
                session.getBasicRemote().sendObject(new InputMessage(msg));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
                e.printStackTrace();
            }
        }

    }

//    private void showList(String target) {
//        String[] list = new String[rooms.get(target).size()];
//        int i = 0;
//        for (Session session : rooms.get(target).keySet()) {
//            list[i] = rooms.get(target).get(session).getUsername();
//            i++;
//        }
//        for (Session session : rooms.get(target).keySet()) {
//            try {
//                session.getBasicRemote().sendObject(new InputMessage(list));
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (EncodeException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private synchronized void addOnlineNum() {
        onlineNum++;
    }

    private synchronized void subOnlineNum() {
        onlineNum--;
    }

    private Integer getOnLineNum() {
        return onlineNum;
    }


}

