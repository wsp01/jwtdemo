package com.wsp.jwt.jwtdemo.exception;

import lombok.Data;

/**
 * @author wsp
 * @title: SecurityException
 * @projectName jwtdemo
 * @description: 异常处理类
 * @date 2019/8/2210:13
 */
@Data
public class SecurityException extends RuntimeException{
    private int code;

    public SecurityException(int code) {
        this.code = code;
    }

    public SecurityException(int code,String message) {
        super(message);
        this.code = code;
    }
}
