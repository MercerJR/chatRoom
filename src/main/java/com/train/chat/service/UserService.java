package com.train.chat.service;

import com.train.chat.dao.UserInfoMapper;
import com.train.chat.dao.UserMapper;
import com.train.chat.data.HttpInfo;
import com.train.chat.data.Message;
import com.train.chat.exception.CustomException;
import com.train.chat.exception.CustomExceptionType;
import com.train.chat.pojo.User;
import com.train.chat.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author MercerJR
 * @Data 2021/4/15 9:36
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserService {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private UserInfoMapper infoMapper;

    public void register(User user) {
        UserInfo userInfo = new UserInfo(user.getUserId(),user.getUsername());
        if (!mapper.insertSelective(user) || !infoMapper.insertSelective(userInfo)){
            throw new CustomException(CustomExceptionType.VALIDATE_ERROR, Message.CONTACT_ADMIN);
        }
    }

    public User login(String username, String password) {
        return mapper.selectByNameAndPass(username,password);
    }

    public List<User> selectUserByInfo(String userInfo) {
        return mapper.selectUserByInfo(userInfo);
    }

    public String updateUserInfo(UserInfo userInfo, String oldName,HttpSession session){
        String returnUsername = null;
        if (userInfo.getUsername() != null){
            String newUsername = userInfo.getUsername();
            User user = mapper.selectByUsername(newUsername);
            if (user != null && !user.getUsername().equals(oldName)){
                throw new CustomException(CustomExceptionType.VALIDATE_ERROR,Message.USERNAME_BE_USED);
            }
            if (!mapper.updateUsernameById(newUsername,userInfo.getUserId())){
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR,Message.CONTACT_ADMIN);
            }
            User sessionUser = (User) session.getAttribute(HttpInfo.USER_SESSION);
            sessionUser.setUsername(newUsername);
            session.setAttribute(HttpInfo.USER_SESSION,sessionUser);
            returnUsername = newUsername;
        }
        if (!infoMapper.updateByPrimaryKeySelective(userInfo)){
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,Message.CONTACT_ADMIN);
        }
        return returnUsername;
    }

    public UserInfo displayUserInfo(String userId) {
        return infoMapper.selectByPrimaryKey(userId);
    }

    public boolean existUsername(String username) {
        return mapper.selectByUsername(username) != null;
    }
}
