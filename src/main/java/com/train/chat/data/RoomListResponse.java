package com.train.chat.data;

import com.train.chat.pojo.Room;
import lombok.Data;

import java.util.List;

/**
 * @Author MercerJR
 * @Data 2020/7/17 15:22
 */
@Data
public class RoomListResponse {
    private List<Room> roomList;
    private Integer size;

    public RoomListResponse(List<Room> roomList){
        this.roomList = roomList;
        this.size = roomList.size();
    }
}
