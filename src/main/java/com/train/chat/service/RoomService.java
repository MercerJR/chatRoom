package com.train.chat.service;

import com.train.chat.dao.RoomMapper;
import com.train.chat.dao.UserAndRoomMapper;
import com.train.chat.pojo.Room;
import com.train.chat.pojo.User;
import com.train.chat.pojo.UserAndRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author MercerJR
 * @Data 2020/7/16 20:50
 */
@Service
public class RoomService {

    @Autowired
    private UserAndRoomMapper mapper;

    @Autowired
    private RoomMapper roomMapper;

    public void createRoom(String userId, String roomId,String roomName) {
        Room room = new Room(roomId,roomName);
        UserAndRoom userAndRoom = new UserAndRoom(userId,roomId);
        roomMapper.insert(room);
        mapper.insert(userAndRoom);
    }

    public void joinExistRoom(String userId,String roomId){
        if (mapper.selectRecord(userId,roomId) == null){
            UserAndRoom userAndRoom = new UserAndRoom(userId,roomId);
            mapper.insert(userAndRoom);
        }
    }

    public boolean existRoom(String room) {
        return roomMapper.existRoom(room) != null;
    }

    public List<Room> displayRoomList(String userId) {
        return roomMapper.selectRoomByUser(userId);
    }

    public List<User> displayUserList(String roomId) {
        return mapper.selectUserByRoom(roomId);
    }

    public List<Room> selectRoomByInfo(String roomInfo) {
        return roomMapper.selectRoomByInfo(roomInfo);
    }

    public List<String> selectRoomIdByUser(String userId) {
        return mapper.selectRoomIdByUser(userId);
    }
}
