package com.train.chat.dao;

import com.train.chat.pojo.UserInfo;

/**
 * @Author MercerJR
 * @Data 2020/7/20 17:03
 */
public interface UserInfoMapper {
    boolean deleteByPrimaryKey(String userId);

    boolean insert(UserInfo record);

    boolean insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(String userId);

    boolean updateByPrimaryKeySelective(UserInfo record);

    boolean updateByPrimaryKey(UserInfo record);
}