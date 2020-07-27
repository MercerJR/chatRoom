package com.train.chat.service;

import com.train.chat.dao.ChatMessageMapper;
import com.train.chat.dao.RoomMapper;
import com.train.chat.dao.UserAndRoomMapper;
import com.train.chat.data.HttpInfo;
import com.train.chat.data.Message;
import com.train.chat.exception.CustomException;
import com.train.chat.exception.CustomExceptionType;
import com.train.chat.pojo.Room;
import com.train.chat.pojo.User;
import com.train.chat.pojo.UserAndRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void createRoom(User user, String roomId, String roomName) {
        Room room = new Room(roomId,roomName);
        UserAndRoom userAndRoom = new UserAndRoom(user.getUserId(),roomId);
        if (!roomMapper.insert(room) || !mapper.insert(userAndRoom)){
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,Message.CONTACT_ADMIN);
        }
        String roomsKey = user.getUserId() + ":" + HttpInfo.REDIS_ROOMS_SUFFIX;
        String membersKey = roomId + ":" + HttpInfo.REDIS_MEMBERS_SUFFIX;
        redisTemplate.opsForList().rightPush(roomsKey,room);
        redisTemplate.opsForList().rightPush(membersKey,user);
    }

    public void joinExistRoom(User user,Room room){
        if (mapper.selectRecord(user.getUserId(),room.getRoomId()) == null){
            UserAndRoom userAndRoom = new UserAndRoom(user.getUserId(),room.getRoomId());
            if (!mapper.insert(userAndRoom)){
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR,Message.CONTACT_ADMIN);
            }
            String roomsKey = user.getUserId() + ":" + HttpInfo.REDIS_ROOMS_SUFFIX;
            String membersKey = room.getRoomId() + ":" + HttpInfo.REDIS_MEMBERS_SUFFIX;
            redisTemplate.opsForList().rightPush(roomsKey,room);
            redisTemplate.opsForList().rightPush(membersKey,user);
        }else {
            throw new CustomException(CustomExceptionType.VALIDATE_ERROR,Message.ALREADY_ADD_ROOM);
        }
    }

    public void exitRoom(User user,String roomId){
        if (roomId.equals(HttpInfo.HALL)){
            throw new CustomException(CustomExceptionType.VALIDATE_ERROR,Message.HALL_CAN_NOT_EXIT);
        }
        Room room = roomMapper.selectByPrimaryKey(roomId);
        if (!mapper.deleteRecord(user.getUserId(),roomId)){
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,Message.CONTACT_ADMIN);
        }
        String membersKey = roomId + ":" + HttpInfo.REDIS_MEMBERS_SUFFIX;
        String roomsKey = user.getUserId() + ":" + HttpInfo.REDIS_ROOMS_SUFFIX;
        redisTemplate.opsForList().remove(membersKey,0,user);
        redisTemplate.opsForList().remove(roomsKey,0,room);
        if (mapper.selectMembersNum(roomId) == 0){
            if (!roomMapper.deleteByPrimaryKey(roomId)){
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR,Message.CONTACT_ADMIN);
            }
            redisTemplate.delete(membersKey);
            chatMessageMapper.deleteMessageByRoom(roomId);
        }
    }

    public boolean existRoom(String room) {
        return roomMapper.existRoom(room) != null;
    }

    public List displayRoomList(String userId) {
        String roomsKey = userId + ":" + HttpInfo.REDIS_ROOMS_SUFFIX;
        List<Object> roomList = redisTemplate.opsForList().range(roomsKey,0,-1);
        if (roomList != null && roomList.size() != 0){
            return roomList;
        }else {
            List rooms = roomMapper.selectRoomByUser(userId);
            if (rooms != null && rooms.size() != 0){
                redisTemplate.opsForList().rightPushAll(roomsKey,rooms);
            }
            return rooms;
        }
    }

    public List displayUserList(String roomId) {
        String membersKey = roomId + ":" + HttpInfo.REDIS_MEMBERS_SUFFIX;
        List<Object> memberList = redisTemplate.opsForList().range(membersKey,0,-1);
        if (memberList != null && memberList.size() != 0){
            return memberList;
        }else {
            List members = mapper.selectUserByRoom(roomId);
            if (members != null && members.size() != 0){
                redisTemplate.opsForList().rightPushAll(membersKey,members);
            }
            return members;
        }
    }

    public List<Room> selectRoomByInfo(String roomInfo) {
        return roomMapper.selectRoomByInfo(roomInfo);
    }

    public List<String> selectRoomIdByUser(String userId) {
        return mapper.selectRoomIdByUser(userId);
    }

    public void addAllMessageTag(String roomId) {
        if (!mapper.updateAllMessageTagToOne(roomId)){
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, Message.CONTACT_ADMIN);
        }
    }

    public void subMessageTag(String userId,String roomId){
        if (!mapper.updateMessageTagToZero(userId,roomId)){
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, Message.CONTACT_ADMIN);
        }
    }

    public void dissolvedRoom(String roomId) {
        if (!roomMapper.deleteByPrimaryKey(roomId) || !mapper.deleteRecordByRoom(roomId)){
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,Message.CONTACT_ADMIN);
        }
    }
}
