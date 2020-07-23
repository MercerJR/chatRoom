package com.train.chat.service;

import com.train.chat.dao.ApplyMessageMapper;
import com.train.chat.dao.FriendMapper;
import com.train.chat.dao.UserMapper;
import com.train.chat.data.HttpInfo;
import com.train.chat.data.Message;
import com.train.chat.exception.CustomException;
import com.train.chat.exception.CustomExceptionType;
import com.train.chat.pojo.ApplyMessage;
import com.train.chat.pojo.Friend;
import com.train.chat.pojo.User;
import com.train.chat.utils.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author MercerJR
 * @Data 2020/7/20 14:34
 */
@Service
public class FriendService {

    @Autowired
    private FriendMapper mapper;

    @Autowired
    private ApplyMessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdUtil idUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void addFriend(String userId, String friendId) {
        if (mapper.isFriend(userId,friendId) == null){
            Friend friend = new Friend(userId,friendId,1);
            String msgId = "M" + idUtil.getPrimaryKey();
            String username = userMapper.selectUsernameById(userId);
            String newMessage = username + "(" + userId+ ")" + Message.ADD_FRIEND;
            if (!mapper.insert(friend) ||
                    !messageMapper.insert(new ApplyMessage(msgId,friendId,userId,newMessage,0))){
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR,Message.CONTACT_ADMIN);
            }
        }else{
            throw new CustomException(CustomExceptionType.VALIDATE_ERROR,Message.ALREADY_FRIEND);
        }
    }

    public void agreeApply(User user, ApplyMessage applyMessage) {
        String userId = user.getUserId();
        String username = user.getUsername();
        String friendId = applyMessage.getApplyId();
        String msgId = "M" + idUtil.getPrimaryKey();
        String newMessage = username + "(" + userId+ ")" + Message.AGREE_MESSAGE;
        ApplyMessage agreeMessage = new ApplyMessage(msgId,applyMessage.getApplyId(),userId,newMessage,1);
        Friend friend = new Friend(userId,applyMessage.getApplyId(),2);
        if (!mapper.updateTag(applyMessage.getApplyId(),userId)
                || !mapper.insert(friend)
                || !messageMapper.deleteByPrimaryKey(applyMessage.getMsgId())
                || !messageMapper.insert(agreeMessage)){
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,Message.CONTACT_ADMIN);
        }
        User friendUser = userMapper.selectByPrimaryKey(friendId);
        String friendsKey = userId + ":" + HttpInfo.REDIS_FRIENDS_SUFFIX;
        String friendsKey2 = friendId + ":" + HttpInfo.REDIS_FRIENDS_SUFFIX;
        redisTemplate.opsForList().rightPush(friendsKey,friendUser);
        redisTemplate.opsForList().rightPush(friendsKey2,user);
    }

    public void refuseApply(String userId, ApplyMessage applyMessage) {
        String msgId = "M" + idUtil.getPrimaryKey();
        String username = userMapper.selectUsernameById(userId);
        String newMessage = username + "(" + userId+ ")" + Message.REFUSE_MESSAGE;
        ApplyMessage agreeMessage = new ApplyMessage(msgId,applyMessage.getApplyId(),userId,newMessage,1);
        if (!mapper.deleteTag(applyMessage.getApplyId(),userId)
                || !messageMapper.deleteByPrimaryKey(applyMessage.getMsgId())
                || !messageMapper.insert(agreeMessage)){
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,Message.CONTACT_ADMIN);
        }
    }

    public List<ApplyMessage> displayMessageList(String userId) {
        return messageMapper.selectByUser(userId);
    }

    public List displayFriends(String userId) {
        String friendsKey = userId + ":" + HttpInfo.REDIS_FRIENDS_SUFFIX;
        List<Object> friendList = redisTemplate.opsForList().range(friendsKey,0,-1);
        if (friendList != null && friendList.size() != 0){
            return friendList;
        }else {
            List friends = mapper.selectFriendsByUser(userId);
            if (friends != null && friends.size() != 0){
                redisTemplate.opsForList().rightPushAll(friendsKey,friends);
            }
            return friends;
        }
    }
}
