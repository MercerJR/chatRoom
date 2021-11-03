package com.train.chat.dao;

import com.train.chat.pojo.User;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @Author MercerJR
 * @Data 2021/4/20 15:44
 */
public interface UserMapper {
    boolean deleteByPrimaryKey(String userId);

    boolean insert(User record);

    boolean insertSelective(User record);

    User selectByPrimaryKey(String userId);

    boolean updateByPrimaryKeySelective(User record);

    boolean updateByPrimaryKey(User record);

    User selectByNameAndPass(@Param("username") String username, @Param("password") String password);

    List<User> selectUserByInfo(String userInfo);

    String selectUsernameById(String userId);

    User selectByUsername(String username);

    boolean updateUsernameById(@Param("username") String username, @Param("userId") String userId);
}