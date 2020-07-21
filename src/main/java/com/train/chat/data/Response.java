package com.train.chat.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.train.chat.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author MercerJR
 * @Data 2020/4/10 11:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private Integer code;
    private String message;
    private Object data;

    public Response success(Object data) {
        Response response = new Response();
        response.setCode(200);
        response.setMessage("操作成功");
        response.setData(data);
        return response;
    }

    public Response success() {
        Response response = new Response();
        response.setCode(200);
        response.setMessage("操作成功");
        return response;
    }

//    public Response fail() {
//        Response response = new Response();
//        response.setCode(500);
//        response.setMessage("操作失败");
//        return response;
//    }
//
//    public Response fail(String message) {
//        Response response = new Response();
//        response.setCode(500);
//        response.setMessage(message);
//        return response;
//    }
//
//    public Response validateFailed() {
//        Response response = new Response();
//        response.setCode(404);
//        response.setMessage("参数校验失败");
//        return response;
//    }

    public Response validateFailed(String message) {
        Response response = new Response();
        response.setCode(404);
        response.setMessage(message);
        return response;
    }

    public Response error(CustomException e) {
        Response response = new Response();
        response.setCode(e.getCode());
        response.setMessage(e.getMessage());
        return response;
    }
}
