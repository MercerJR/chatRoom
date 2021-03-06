var webSocket;
chatMessage = [];
var rooms = [];
users = [];
var rightTemp;
var tag = 0;
var centerTag = 0;
centerBoard = [];
var rightTag = 0;
rightBoard = [];
var currentSelectUserId;

function showUserInfo(user) {
    rightBoard[rightTag] = users;
    rightTemp = document.getElementById("userListBoard").innerHTML;
    var userId = user.split("(")[1].split(")")[0];
    currentSelectUserId = userId;
    var currentUserId = document.getElementById("user").innerHTML.split("(")[1].split(")")[0];
    var req = new XMLHttpRequest();
    //设置请求方法 主机地址，是否异步
    req.open("get", "/user/selectUserInfo/" + userId, true);
    req.send();
    req.onreadystatechange = function () {
        //console.log(req.readyState + " " + req.status);
        if (req.readyState == 4 && req.status == 200) {
            var userInfo = JSON.parse(req.responseText).data;
            var infoDisplay = '<div class="card" style="margin-top: 2rem">';
            infoDisplay += '<div class="card-header" id="cardHeader">' + user + "的个人信息" + '</div>';
            infoDisplay += '<ul class="list-group list-group-flush">';
            infoDisplay += '<li class="list-group-item">' + '用户名：' + userInfo.username + '</li>';
            infoDisplay += '<li class="list-group-item">' + '性别：' + userInfo.gender + '</li>';
            infoDisplay += '<li class="list-group-item">' + '年龄：' + userInfo.age + '</li>';
            infoDisplay += '<li class="list-group-item">' + '爱好：' + userInfo.hobby + '</li>';
            infoDisplay += '<li class="list-group-item">' + '家乡：' + userInfo.hometown + '</li>';
            infoDisplay += '<li class="list-group-item">' + '个人描述：' + userInfo.describe + '</li>';
            infoDisplay += '</ul>';
            infoDisplay += '</div>';
            infoDisplay += '<button type="button" class="btn btn-info" style="margin-top: 1rem" onclick="back()">返回</button>';
            if (currentUserId !== currentSelectUserId) {
                infoDisplay += '<button type="button" class="btn btn-success" style="margin-top: 1rem" onclick="addFriend()">加好友</button>';
            }
            document.getElementById("userListBoard").innerHTML = infoDisplay;
            rightTag = 1;
        }
    };
}

function back() {
    rightTag = 0;
    document.getElementById("userListBoard").innerHTML = rightTemp;
}

function updateUserInfo() {
    var req = new XMLHttpRequest();
    //设置请求方法 主机地址，是否异步
    req.open("get", "/user/displayUserInfo/", true);
    req.send();
    req.onreadystatechange = function () {
        //console.log(req.readyState + " " + req.status);
        if (req.readyState == 4 && req.status == 200) {
            var userInfo = JSON.parse(req.responseText).data;
            centerBoard[centerTag] = document.getElementById("center").innerHTML;
            var infoBoard = '<h4 style="margin-top: 2rem">个人信息</h4>';
            infoBoard += '<div class="form-group">\n' +
                '    <label >用户名</label>\n' +
                '    <input type="text" placeholder="' + userInfo.username + '" class="form-control" id="inputUsername">\n' +
                '  </div>';
            infoBoard += '<div class="form-group">\n' +
                '    <label >性别</label>\n' +
                '    <input type="text" placeholder="' + userInfo.gender + '" class="form-control" id="inputGender">\n' +
                '  </div>';
            infoBoard += '<div class="form-group">\n' +
                '    <label >年龄</label>\n' +
                '    <input type="text" placeholder="' + userInfo.age + '" class="form-control" id="inputAge">\n' +
                '  </div>';
            infoBoard += '<div class="form-group">\n' +
                '    <label >爱好</label>\n' +
                '    <input type="text" placeholder="' + userInfo.hobby + '" class="form-control" id="inputHobby">\n' +
                '  </div>';
            infoBoard += '<div class="form-group">\n' +
                '    <label >家乡</label>\n' +
                '    <input type="text" placeholder="' + userInfo.hometown + '" class="form-control" id="inputHometown">\n' +
                '  </div>';
            infoBoard += '<div class="form-group">\n' +
                '    <label >个人描述</label>\n' +
                '    <input type="text" placeholder="' + userInfo.describe + '" class="form-control" id="inputDescribe">\n' +
                '  </div>';
            infoBoard += '<button type="submit" class="btn btn-primary" onclick="updateInfo()">提交修改</button>';
            document.getElementById("center").innerHTML = infoBoard;
            centerTag = 2
        }
    };
}

function select() {
    centerBoard[centerTag] = document.getElementById("center").innerHTML;
    var selectBoard = '';
    selectBoard += '<div class="input-group mb-3" style="margin-bottom: 2rem;margin-top: 1rem;">\n' +
        '                <input type="text" id="selectRoom" class="form-control" placeholder="输入您想要搜索的房间名称或id"\n' +
        '                       aria-label="Recipient\'s username" aria-describedby="basic-addon2">\n' +
        '                <div class="input-group-append">\n' +
        '                    <button class="btn btn-outline-secondary" type="button"\n' +
        '                            onclick="selectRoom(document.getElementById(\'selectRoom\').value)">搜索\n' +
        '                    </button>\n' +
        '                </div>\n' +
        '            </div>';
    selectBoard += '<div class="input-group mb-3" style="margin-bottom: 2rem;margin-top: 1rem;">\n' +
        '                <input type="text" id="selectUser" class="form-control" placeholder="输入您想要搜索的用户名称或id"\n' +
        '                       aria-label="Recipient\'s username" aria-describedby="basic-addon2">\n' +
        '                <div class="input-group-append">\n' +
        '                    <button class="btn btn-outline-secondary" type="button"\n' +
        '                            onclick="selectUser(document.getElementById(\'selectUser\').value)">搜索\n' +
        '                    </button>\n' +
        '                </div>\n' +
        '            </div>';
    selectBoard += '<div id="selectList"></div>';
    document.getElementById("center").innerHTML = selectBoard;
    centerTag = 1;
}

function chatWithUser(user) {
    if (centerTag !== 0) {
        centerBoard[centerTag] = document.getElementById("center").innerHTML;
        document.getElementById("center").innerHTML = centerBoard[0];
        centerTag = 0;
    }
    var username = user.split("(")[0];
    var userId = user.split("(")[1].split(")")[0];
    tag = rooms.indexOf(userId);
    if (chatMessage[tag] == null) {
        document.getElementsByClassName("lead")[0].innerHTML = "";
    } else {
        document.getElementsByClassName("lead")[0].innerHTML = chatMessage[tag];
    }
    alert("你已与 " + username + " 开始聊天");
    showUserInfo(user);
}

function enterRoom(room) {
    if (centerTag !== 0) {
        centerBoard[centerTag] = document.getElementById("center").innerHTML;
        document.getElementById("center").innerHTML = centerBoard[0];
        centerTag = 0;
    }
    // if (rightTag !== 0){
    //     rightBoard[rightTag] = document.getElementById("userListBoard").innerHTML;
    //     document.getElementById("userListBoard").innerHTML = rightBoard[0];
    // }
    var roomName;
    if (room === "大厅") {
        tag = 0;
        roomName = "大厅";
    } else {
        roomName = room.split("(")[0];
        var roomId = room.split("(")[1].split(")")[0];
        var roomObj = {"roomId": roomId, "roomName": roomName};
        joinRoom(roomObj);
        tag = rooms.indexOf(roomId);
    }
    if (chatMessage[tag] == null) {
        document.getElementsByClassName("lead")[0].innerHTML = "";
    } else {
        document.getElementsByClassName("lead")[0].innerHTML = chatMessage[tag];
    }
    if (users[tag] == null) {
        document.getElementById("userList").innerHTML = "";
    } else {
        document.getElementById("userList").innerHTML = users[tag];
    }
    alert("你已进入 " + roomName);
    rightTag = 0;
    displayUserList();
}

function send() {
    if (webSocket != null) {
        var input_msg = document.getElementById("inputMessage").value.trim();
        if (input_msg == "") {
            return;
        }
        var message = {"target": rooms[tag], "message": input_msg, "type": 0};
        var json = JSON.stringify(message);
        webSocket.send(json);
        // 清除input框里的信息
        document.getElementById("inputMessage").value = "";
    } else {
        alert("您已掉线，请重新进入聊天室...");
    }
}

function closeWS() {
    webSocket.close();
}

function initWebSocket() {
    if ("WebSocket" in window) {
        if (webSocket == null) {
            var url = "ws://localhost:8082/hall";
            // 打开一个 web socket
            webSocket = new WebSocket(url);
        } else {
            alert("您已进入大厅");
        }

        webSocket.onopen = function () {
            alert("您已进入大厅，一起嗨皮");
        };

        webSocket.onmessage = function (evt) {
            var msgObj = JSON.parse(evt.data);
            if (msgObj.type === 0) {
                var roomTag = rooms.indexOf(msgObj.target);
                var newMessage = msgObj.message;
                var oldMessage = chatMessage[roomTag];
                if (oldMessage == null) {
                    oldMessage = "";
                }
                chatMessage[roomTag] = oldMessage + newMessage + "<br>";
                chatMessage[roomTag].scrollTop = chatMessage[roomTag].scrollTop + 40;
                if (roomTag === tag){
                    document.getElementsByClassName("lead")[0].innerHTML = chatMessage[roomTag];
                }
            } else {
                var userListBoard = '<h4 style="margin-top: 2rem">在线列表</h4>';
                userListBoard += '<ul class="list-group" id="userList" style="margin-top: 1rem">';
                for (var i = 0; i < msgObj.listNum; i++) {
                    var username = msgObj.list[i].username;
                    var userId = msgObj.list[i].userId;
                    userListBoard += '<li class="list-group-item list-group-item-action" onclick="showUserInfo(this.innerHTML)">' +
                        username + '(' + userId + ')' + '</li>'
                }
                userListBoard += '</ul>';
                users[tag] = userListBoard;
                document.getElementById("userListBoard").innerHTML = users[tag];
            }
        };

        webSocket.onclose = function () {
            // 关闭 websocket，清空信息板
            alert("连接已关闭");
            webSocket = null;
            document.getElementsByClassName("lead")[0].innerHTML = "";
            document.getElementById("groupList").innerHTML = "";
            document.getElementById("userListBoard").innerHTML = "";
        };
    } else {
        // 浏览器不支持 WebSocket
        alert("您的浏览器不支持 WebSocket!");
    }
}

function addFriend() {
    $.ajax({
        url: "/friend/addFriend",
        type: "POST",
        data: currentSelectUserId,
        dataType: "json",//返回值类型 JSON
        contentType: 'application/json',
        success: function (res) {  //请求成功的回调
            if (res.code === 200) {
                alert("已发送好友申请");
            } else {
                alert(res.message)
            }
        },
        error: function (res) { //请求失败的回调
            alert(res.result)
        }
    })
}

function updateInfo() {
    var params = {
        "username": $("#inputUsername").val(),//取出文本中的值
        "gender": $("#inputGender").val(),
        "age": $("#inputAge").val(),
        "hobby": $("#inputHobby").val(),
        "hometown": $("#inputHometown").val(),
        "describe": $("#inputDescribe").val()
    };
    $.ajax({
        url: "/user/updateInfo",
        type: "PUT",
        data: JSON.stringify(params),
        dataType: "json",//返回值类型 JSON
        contentType: 'application/json',
        success: function (res) {  //请求成功的回调
            if (res.code === 200) {
                alert(res.message)
            } else {
                alert(res.message)
            }
        },
        error: function (res) { //请求失败的回调
            alert(res.result)
        }
    })
}

function cancellation() {
    $.ajax({
        url: "/user/cancellation",
        type: "POST",
        dataType: "json",//返回值类型 JSON
        contentType: 'application/json',
        success: function (res) {  //请求成功的回调
            if (res.code === 200) {
                alert("您已注销");
            } else {
                alert(res.message)
            }
        },
        error: function (res) { //请求失败的回调
            alert(res.result)
        }
    })
}

function createRoom(roomName) {
    $.ajax({
        url: "/room/createRoom",
        data: roomName,
        type: "POST",
        dataType: "json",//返回值类型 JSON
        contentType: 'application/json',
        success: function (res) {  //请求成功的回调
            if (res.code === 200) {
                alert("你已创建房间：" + roomName);
                displayRoomList();
            } else {
                alert(res.message)
            }
        },
        error: function (res) { //请求失败的回调
            alert(res.result)
        }
    })
}

function joinRoom(roomObj) {
    $.ajax({
        url: "/room/enterRoom",
        data: JSON.stringify(roomObj),
        type: "POST",
        dataType: "json",//返回值类型 JSON
        contentType: 'application/json',
        success: function (res) {  //请求成功的回调
            if (res.code === 200) {

            } else {
                alert(res.message)
            }
        },
        error: function (res) { //请求失败的回调
            alert(res.result)
        }
    })
}

function joinExistRoom(room) {
    var roomId = room.split("(")[1].split(")")[0];
    var roomName = room.split("(")[0];
    var roomObj = {"roomId": roomId,"roomName": roomName};
    $.ajax({
        url: "/room/joinExistRoom",
        data: JSON.stringify(roomObj),
        type: "POST",
        dataType: "json",//返回值类型 JSON
        contentType: 'application/json',
        success: function (res) {  //请求成功的回调
            if (res.code === 200) {
                displayRoomList();
                enterRoom(room);
            } else {
                alert(res.message)
            }
        },
        error: function (res) { //请求失败的回调
            alert(res.result)
        }
    })
}

function exitRoom(roomId) {
    $.ajax({
        url: "/room/exitRoom",
        data: roomId,
        type: "POST",
        dataType: "json",//返回值类型 JSON
        contentType: 'application/json',
        success: function (res) {  //请求成功的回调
            if (res.code === 200) {
                displayRoomList();
            } else {
                alert(res.message)
            }
        },
        error: function (res) { //请求失败的回调
            alert(res.result)
        }
    })
}

function agreeApply(msgTag){
    msgTag = msgTag.split(":")[1];
    $.ajax({
        url: "/friend/agreeApply",
        data: JSON.stringify(messageList[msgTag]),
        type: "POST",
        dataType: "json",//返回值类型 JSON
        contentType: 'application/json',
        success: function (res) {  //请求成功的回调
            if (res.code === 200) {
                alert("好友添加成功");
                displayFriends()
            } else {
                alert(res.message)
            }
        },
        error: function (res) { //请求失败的回调
            alert(res.result)
        }
    });
    displayMessage();
}

function refuseApply(msgTag){
    msgTag = msgTag.split(":")[1];
    $.ajax({
        url: "/friend/refuseApply",
        data: JSON.stringify(messageList[msgTag]),
        type: "POST",
        dataType: "json",//返回值类型 JSON
        contentType: 'application/json',
        success: function (res) {  //请求成功的回调
            if (res.code === 200) {
                alert("已拒绝好友申请")
            } else {
                alert(res.message)
            }
        },
        error: function (res) { //请求失败的回调
            alert(res.result)
        }
    });
    displayMessage();
}

selectUsers = [];

function selectUser(userInfo) {
    var req = new XMLHttpRequest();
    //设置请求方法 主机地址，是否异步
    req.open("get", "/user/selectUser/" + userInfo, true);
    req.send();
    req.onreadystatechange = function () {
        //console.log(req.readyState + " " + req.status);
        if (req.readyState == 4 && req.status == 200) {
            var selectList = '<div class="list-group">';
            var content = JSON.parse(req.responseText).data;
            for (var i = 0; i < content.size; i++) {
                var username = content.userList[i].username;
                var userId = content.userList[i].userId;
                selectUsers[i] = userId;
                selectList += '<li class="list-group-item list-group-item-action" onclick="showUserInfo(this.innerHTML)">' +
                    username + '(' + userId + ')' + '</li>';
            }
            selectList += '</div>';
            document.getElementById("selectList").innerHTML = selectList;
        }
    }
}

selectRooms = [];

function selectRoom(roomInfo) {
    var req = new XMLHttpRequest();
    //设置请求方法 主机地址，是否异步
    req.open("get", "/room/selectRoom/" + roomInfo, true);
    req.send();
    req.onreadystatechange = function () {
        //console.log(req.readyState + " " + req.status);
        if (req.readyState == 4 && req.status == 200) {
            var selectList = '<div class="list-group">';
            var content = JSON.parse(req.responseText).data;
            for (var i = 0; i < content.size; i++) {
                var roomName = content.roomList[i].roomName;
                var roomId = content.roomList[i].roomId;
                selectRooms[i] = roomId;
                selectList += '<li class="list-group-item list-group-item-action" onclick="joinExistRoom(this.innerHTML)">' +
                    roomName + '(' + roomId + ')' + '</li>';
            }
            selectList += '</div>';
            document.getElementById("selectList").innerHTML = selectList;
        }
    }
}

messageList = [];
function displayMessage() {
    centerBoard[centerTag] = document.getElementById("center").innerHTML;
    var msgBoard = '<h4 style="margin-top: 2rem">消息通知</h4>';
    var req = new XMLHttpRequest();
    //设置请求方法 主机地址，是否异步
    req.open("get", "/friend/displayMessage/", true);
    req.send();
    req.onreadystatechange = function () {
        //console.log(req.readyState + " " + req.status);
        if (req.readyState == 4 && req.status == 200) {
            var content = JSON.parse(req.responseText).data;
            if (content.size === 0){
                msgBoard += '<div class="card">\n' +
                    '  <div class="card-body">\n' +
                    '    暂时没有消息哦\n' +
                    '  </div>\n' +
                    '</div>';
            }
            for (var i = 0;i < content.size;i++){
                msgBoard += '<div class="card" style="margin-top: 1rem">\n' +
                    '  <div class="card-body">';
                msgBoard += content.applyMessageList[i].message;
                msgBoard += '</div>\n' +
                    '</div>';
                var agree = "agree:" + i;
                var refuse = "refuse:" + i;
                if (content.applyMessageList[i].type === 0){
                    msgBoard += '<button type="button" id="'+ agree +'" class="btn btn-outline-success"' +
                        ' style="margin-top: 1rem" onclick="agreeApply(this.id)">同意</button>';
                    msgBoard += '<button type="button" id="'+ refuse +'" class="btn btn-outline-danger"' +
                        ' style="margin-top: 1rem" onclick="refuseApply(this.id)">拒绝</button>';
                }
                messageList[i] = content.applyMessageList[i];
            }
            document.getElementById("center").innerHTML = msgBoard;
            centerTag = 3;
        }
    };
}

var roomlength;
function displayRoomList() {
    var req = new XMLHttpRequest();
    //设置请求方法 主机地址，是否异步
    req.open("get", "room/displayRoomList", true);
    req.send();
    req.onreadystatechange = function () {
        //console.log(req.readyState + " " + req.status);
        if (req.readyState == 4 && req.status == 200) {
            rooms = [];
            rooms[0] = "大厅";
            var listBoard = "";
            listBoard += '<li class="list-group-item list-group-item-action" onclick="enterRoom(this.innerHTML)">' +
                '大厅' + '</li>';
            var content = JSON.parse(req.responseText);
            for (var i = 0; i < content.data.size; i++) {
                var roomName = content.data.roomList[i].roomName;
                var roomId = content.data.roomList[i].roomId;
                rooms[rooms.length] = roomId;
                listBoard += '<li class="list-group-item list-group-item-action" onclick="enterRoom(this.innerHTML)">' +
                    roomName + '(' + roomId + ')' + '</li>'
            }
            document.getElementById("roomListBoard").innerHTML = listBoard;
            roomlength = content.data.size;
            displayFriends();
        }
    }
}

function displayFriends() {
    var req = new XMLHttpRequest();
    //设置请求方法 主机地址，是否异步
    req.open("get", "friend/displayFriends", true);
    req.send();
    req.onreadystatechange = function () {
        if (req.readyState == 4 && req.status == 200) {
            var board = "";
            var content = JSON.parse(req.responseText).data;
            for (var i = 0; i < content.size; i++) {
                var username = content.userList[i].username;
                var userId = content.userList[i].userId;
                board += '<li class="list-group-item list-group-item-action" onclick="chatWithUser(this.innerHTML)">' +
                    username + '(' + userId + ')' + '</li>';
                rooms[roomlength + i] = userId;
                document.getElementById("friendListBoard").innerHTML = board;
            }
        }
    }
}

function displayUserList() {
    var req = new XMLHttpRequest();
    //设置请求方法 主机地址，是否异步
    req.open("get", "room/displayUserList/" + rooms[tag], true);
    req.send();
    req.onreadystatechange = function () {
        //console.log(req.readyState + " " + req.status);
        if (req.readyState == 4 && req.status == 200) {
            if (tag === 0) {
                document.getElementById("userListBoard").innerHTML = users[tag];
            } else {
                var userListBoard = '<h4 style="margin-top: 2rem">房间成员列表</h4>';
                userListBoard += '<ul class="list-group" id="userList" style="margin-top: 1rem">';
                var content = JSON.parse(req.responseText).data;
                for (var i = 0; i < content.size; i++) {
                    var username = content.userList[i].username;
                    var userId = content.userList[i].userId;
                    userListBoard += '<li class="list-group-item list-group-item-action" onclick="showUserInfo(this.innerHTML)">' +
                        username + '(' + userId + ')' + '</li>'
                }
                userListBoard += '</ul>';
                users[tag] = userListBoard;
                document.getElementById("userListBoard").innerHTML = users[tag];
            }
        }
    }
}

displayRoomList();
initWebSocket();