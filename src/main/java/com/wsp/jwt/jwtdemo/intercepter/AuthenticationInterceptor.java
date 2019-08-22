package com.wsp.jwt.jwtdemo.intercepter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.wsp.jwt.jwtdemo.annotation.CheckToken;
import com.wsp.jwt.jwtdemo.annotation.LoginToken;
import com.wsp.jwt.jwtdemo.domain.User;
import com.wsp.jwt.jwtdemo.exception.SecurityException;
import com.wsp.jwt.jwtdemo.util.JwtUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


/**
 *@author wsp
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有LoginToken注释，有则跳过认证
        if (method.isAnnotationPresent(LoginToken.class)) {
            LoginToken loginToken = method.getAnnotation(LoginToken.class);
            if (loginToken.required()) {
                return true;
            }
        }

        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(CheckToken.class)) {
            CheckToken checkToken = method.getAnnotation(CheckToken.class);
            if (checkToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new SecurityException(1001,"无token，请重新登录");
                }
                // 获取 token 中的 user id
                String userId = null;
                try {
                    userId = JWT.decode(token).getClaim("id").asString();
                } catch (JWTDecodeException j) {
                    throw new SecurityException(1002,"非法请求！");
                }
                //根据业务情况判断是否需要根据userId去数据库判断该用户信息是否存在
                User user = new User(userId,"wsp","123456");
                if (user == null) {
                    throw new SecurityException(1005,"用户不存在，请重新登录");
                }
                Boolean verify = JwtUtil.isVerify(token);
                if (!verify) {
                    throw new SecurityException(1003,"非法请求！");
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("post handle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println("after completion");
    }

}