package com.train.chat.controller;

import com.train.chat.data.HttpInfo;
import com.train.chat.data.Response;
import com.train.chat.data.RoomListResponse;
import com.train.chat.data.UserListResponse;
import com.train.chat.pojo.Room;
import com.train.chat.pojo.User;
import com.train.chat.service.RoomService;
import com.train.chat.utils.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author MercerJR
 * @Data 2021/4/18 8:34
 */
@RestController
@Slf4j
@RequestMapping("/room")
@Validated
public class RoomController {

    @Autowired
    private RoomService service;

    @Autowired
    private IdUtil idUtil;

    @PostMapping(value = "/enterRoom", produces = "application/json")
    public Response enterRoom(@RequestBody Room room, HttpSession session) {
        User user = (User) session.getAttribute(HttpInfo.USER_SESSION);
        HallController.putRooms(room.getRoomId(), user);
        return new Response().success();
    }

    @PostMapping(value = "/createRoom", produces = "application/json")
    public Response joinRoom(@RequestBody String roomName, HttpSession session) {
        User user = (User) session.getAttribute(HttpInfo.USER_SESSION);
        String roomId = "R" + idUtil.getPrimaryKey();
        service.createRoom(user, roomId, roomName);
        HallController.putRooms(roomId, user);
        return new Response().success(roomId);
    }

    @GetMapping(value = "/selectRoom/{roomInfo}",produces = "application/json")
    public Response selectRoom(@PathVariable("roomInfo") String roomInfo){
        List<Room> roomList = service.selectRoomByInfo(roomInfo);
        return new Response().success(new RoomListResponse(roomList));
    }

    @PostMapping(value = "/joinExistRoom",produces = "application/json")
    public Response joinExistRoom(@RequestBody Room room,HttpSession session){
        User user = (User) session.getAttribute(HttpInfo.USER_SESSION);
        service.joinExistRoom(user,room);
        HallController.putRooms(room.getRoomId(),user);
        return new Response().success();
    }

    @PostMapping(value = "/exitRoom",produces = "application/json")
    public Response exitRoom(@RequestBody String roomId,HttpSession session){
        User user = (User) session.getAttribute(HttpInfo.USER_SESSION);
        service.exitRoom(user,roomId);
        HallController.exitRoom(roomId,user);
        return new Response().success();
    }

    @GetMapping(value = "/displayRoomList",produces = "application/json")
    public Response displayRoomList(HttpSession session){
        User user = (User) session.getAttribute(HttpInfo.USER_SESSION);
        List<Room> roomList = service.displayRoomList(user.getUserId());
        return new Response().success(new RoomListResponse(roomList));
    }

    @GetMapping(value = "/displayUserList/{roomId}",produces = "application/json")
    public Response displayUserList(@PathVariable("roomId") String roomId){
        List<User> userList = service.displayUserList(roomId);
        return new Response().success(new UserListResponse(userList));
    }

//    @PutMapping(value = "/subMessageTag",produces = "application/json")
//    public Response subMessageTag(HttpSession session,@RequestBody String roomId){
//        User user = (User) session.getAttribute(HttpInfo.USER_SESSION);
//        service.subMessageTag(user.getUserId(),roomId);
//        return new Response().success();
//    }
}
