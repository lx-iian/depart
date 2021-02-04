package com.james.depart.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MyResult<T> implements Serializable{

    private static final String MSG_SUCCESS = "SUCCESS";
    private static final String MSG_ERROR = "ERROR";

    private Integer retCode;
    private String retMsg;
    private T retData;

    public static <T> MyResult<T> success(Integer retCode, String retMsg, T data) {
        return new MyResult<>(retCode, retMsg, data);
    }

    public static <T> MyResult<T> success(String retMsg, T data) {
        return new MyResult<>(ResultCode.SUCCESS.getCode(), retMsg, data);
    }

    public static <T> MyResult<T> success(T data) {
        return success(MSG_SUCCESS, data);
    }

    public static <T> MyResult<T> success() {
        return success();
    }

    public static <T> MyResult<T> error(Integer retCode, String retMsg, T data) {
        return new MyResult<>(retCode, retMsg, data);
    }

    public static <T> MyResult<T> error(Integer retCode, String retMsg) {
        return new MyResult<>(retCode, retMsg, null);
    }

    public static <T> MyResult<T> error(String retMsg, T data) {
        return new MyResult<T>(ResultCode.FAIL.getCode(), retMsg, data);
    }

    public static <T> MyResult<T> error(String retMsg ) {
        return new MyResult<>(ResultCode.FAIL.getCode(), retMsg, null);
    }

    public static <T> MyResult<T> error() {
        return error(MSG_ERROR);
    }
}
