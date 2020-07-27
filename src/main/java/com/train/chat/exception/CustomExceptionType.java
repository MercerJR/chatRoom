package com.train.chat.exception;

/**
 * @author Mercer JR
 */

public enum CustomExceptionType {
    /**
     * 系统内部错误
     */
    SYSTEM_ERROR(500,"系统内部错误"),
    /**
     * 参数校验错误
     */
    VALIDATE_ERROR(400,"参数校验错误"),
    /**
     * 未知错误
     */
    UNKNOWN_ERROR(999,"未知错误");

    private Integer code;
    private String typeDesc;


    CustomExceptionType(Integer code, String typeDesc){
        this.code = code;
        this.typeDesc = typeDesc;
    }

    public String getTypeDesc(){
        return this.typeDesc;
    }

    public Integer getCode(){
        return this.code;
    }
}