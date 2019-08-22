package com.wsp.jwt.jwtdemo.domain;

import lombok.Data;

/**
 * 〈user〉
 *
 * @author wsp
 * @create 2019/8/21
 */
@Data
public class User {
    private String id;
    private String userName;
    private String password;

    public User(String id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }
}
