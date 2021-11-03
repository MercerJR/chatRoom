package com.train.chat.dao;

import com.train.chat.pojo.ApplyMessage;import java.util.List;

/**
 * @Author MercerJR
 * @Data 2021/4/27 16:19
 */
public interface ApplyMessageMapper {
    boolean deleteByPrimaryKey(String msgId);

    boolean insert(ApplyMessage record);

    boolean insertSelective(ApplyMessage record);

    ApplyMessage selectByPrimaryKey(String msgId);

    boolean updateByPrimaryKeySelective(ApplyMessage record);

    boolean updateByPrimaryKey(ApplyMessage record);

    List<ApplyMessage> selectByUser(String userId);
}