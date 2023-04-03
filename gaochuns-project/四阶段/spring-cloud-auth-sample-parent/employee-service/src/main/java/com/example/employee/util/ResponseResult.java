package com.example.employee.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult<T> {
    private int status;
    private String msg;
    private T data;

    public ResponseResult(int status, String msg) {
        this(status, msg, null);
    }

    public ResponseResult(ResultCode code, T data) {
        this(code.getCode(), code.getMessage(), data);
    }

    public static ResponseResult<Void> error(ResultCode resultCode) {
        return new ResponseResult<>(resultCode.getCode(), resultCode.getMessage());
    }

    public static ResponseResult<Void> SUCCESS = new ResponseResult<>(200, "成功");
    public static ResponseResult<Void> INNER_ERROR = new ResponseResult<>(500, "服务器错误");
    public static ResponseResult<Void> NOT_FOUND = new ResponseResult<>(404, "未找到");

}
