package com.train.chat.data;

/**
 * @Author MercerJR
 * @Data 2021/4/15 9:34
 */
public interface Message {

    String NAME_PASS_EMPTY = "用户名或密码为空";
    String CONTACT_ADMIN = "系统内部错误，请联系管理员";
    String NAME_PASS_ERROR = "用户名或密码输入错误";
    String ADD_FRIEND = "请求添加你为好友";
    String ALREADY_FRIEND = "你们已经是好友了，不用重复申请";
    String AGREE_MESSAGE = "已同意添加你为好友";
    String REFUSE_MESSAGE = "拒绝添加你为好友";
    String USERNAME_BE_USED = "用户名已被使用";
    String HALL_CAN_NOT_EXIT = "大厅不能退出";
    String ALREADY_ADD_ROOM = "你已加入该房间";
    String ALREADY_APPLY = "你已发送过好友申请，请等待对方回复";
}
