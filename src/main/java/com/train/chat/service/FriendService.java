package com.train.chat.service;

import com.train.chat.dao.ApplyMessageMapper;
import com.train.chat.dao.ChatMessageMapper;
import com.train.chat.dao.FriendMapper;
import com.train.chat.dao.UserMapper;
import com.train.chat.data.Message;
import com.train.chat.exception.CustomException;
import com.train.chat.exception.CustomExceptionType;
import com.train.chat.messagequeue.RabbitProducer;
import com.train.chat.pojo.ApplyMessage;
import com.train.chat.pojo.Friend;
import com.train.chat.pojo.User;
import com.train.chat.utils.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author MercerJR
 * @Data 2021/4/20 14:34
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class FriendService {

    @Autowired
    private FriendMapper mapper;

    @Autowired
    private ApplyMessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RabbitProducer rabbitProducer;

    @Autowired
    private IdUtil idUtil;

    public void addFriend(String userId, String friendId) {
        Friend friendRecord = mapper.selectFriendRecord(userId, friendId);
        if (friendRecord == null) {
            Friend friend = new Friend(userId, friendId, 1);
            String msgId = "M" + idUtil.getPrimaryKey();
            String username = userMapper.selectUsernameById(userId);
            String newMessage = username + "(" + userId + ")" + Message.ADD_FRIEND;
            if (!mapper.insert(friend) ||
                    !messageMapper.insert(new ApplyMessage(msgId, friendId, userId,
                            newMessage, 0, System.currentTimeMillis()))) {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR, Message.CONTACT_ADMIN);
            }
        } else {
            if (friendRecord.getTag() == 1){
                throw new CustomException(CustomExceptionType.VALIDATE_ERROR,Message.ALREADY_APPLY);
            }else {
                throw new CustomException(CustomExceptionType.VALIDATE_ERROR, Message.ALREADY_FRIEND);
            }
        }
    }

    public void agreeApply(User user, ApplyMessage applyMessage) {
        String userId = user.getUserId();
        String username = user.getUsername();
        String friendId = applyMessage.getApplyId();
        String msgId = "M" + idUtil.getPrimaryKey();
        String newMessage = username + "(" + userId + ")" + Message.AGREE_MESSAGE;
        ApplyMessage agreeMessage = new ApplyMessage(msgId, friendId,
                userId, newMessage, 1, System.currentTimeMillis());
        Friend friend = new Friend(userId, friendId, 2);
        if (!mapper.updateTag(friendId, userId) || !mapper.insert(friend)
                || !messageMapper.deleteByPrimaryKey(applyMessage.getMsgId())
                || !messageMapper.insert(agreeMessage)) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, Message.CONTACT_ADMIN);
        }
        rabbitProducer.deleteFriendMessage(userId,friendId);
    }

    public void refuseApply(String userId, ApplyMessage applyMessage) {
        String msgId = "M" + idUtil.getPrimaryKey();
        String friendId = applyMessage.getApplyId();
        String username = userMapper.selectUsernameById(userId);
        String newMessage = username + "(" + userId + ")" + Message.REFUSE_MESSAGE;
        ApplyMessage agreeMessage = new ApplyMessage(msgId, friendId,
                userId, newMessage, 1, System.currentTimeMillis());
        if (!mapper.deleteTag(friendId, userId)
                || !messageMapper.deleteByPrimaryKey(applyMessage.getMsgId())
                || !messageMapper.insert(agreeMessage)) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, Message.CONTACT_ADMIN);
        }
    }

    public List<ApplyMessage> displayMessageList(String userId) {
        return messageMapper.selectByUser(userId);
    }

    public List<User> displayFriends(String userId) {
        return mapper.selectFriendsByUser(userId);
    }

    public void deleteFriend(String userId, String friendId) {
        if (!mapper.deleteRecord(userId, friendId) || !mapper.deleteRecord(friendId, userId)) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR, Message.CONTACT_ADMIN);
        }
        rabbitProducer.deleteFriendMessage(userId,friendId);
    }

    public boolean isFriend(String userId,String friendId){
        return mapper.selectFriendRecord(userId, friendId) != null;
    }
}
