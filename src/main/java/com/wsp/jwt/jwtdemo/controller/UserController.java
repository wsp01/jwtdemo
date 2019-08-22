package com.wsp.jwt.jwtdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.wsp.jwt.jwtdemo.annotation.CheckToken;
import com.wsp.jwt.jwtdemo.annotation.LoginToken;
import com.wsp.jwt.jwtdemo.domain.User;
import com.wsp.jwt.jwtdemo.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

/**
 * @author wsp
 */
@RestController
@RequestMapping("/api")
public class UserController {

    //登录
    @PostMapping("/login")
    @LoginToken(required = true)
    public Object login(@RequestBody User user) {
        JSONObject jsonObject = new JSONObject();
        User userEntity = new User(user.getId(),"wsp","123456");
        if (userEntity == null) {
            jsonObject.put("message", "登录失败,用户不存在");
            return jsonObject;
        } else {
            if (!userEntity.getPassword().equals(user.getPassword())) {
                jsonObject.put("message", "登录失败,密码错误");
                return jsonObject;
            } else {
                String token = JwtUtil.createJWT(6000000, userEntity);
                jsonObject.put("token", token);
                jsonObject.put("user", userEntity);
                return jsonObject;
            }
        }
    }

    //查看详情
    @CheckToken
    @GetMapping("/getMessage")
    public String getMessage() {
        return "你已通过验证";
    }


}