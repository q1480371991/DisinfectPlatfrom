package com.example.disinfectplatfrom.Handler;

import com.example.disinfectplatfrom.Pojo.User;
import com.example.disinfectplatfrom.Utils.R;
import com.example.disinfectplatfrom.Utils.code;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Lin
 * @version : [v1.0]
 * @className : MyAuthenticationSuccessHandler
 * @description : 认证成功处理器
 * @createTime : 2023/6/19 18:38
 * @updateUser : Lin
 * @updateTime : 2023/6/19 18:38
 * @updateRemark : 认证成功处理器
 */
@Component

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        R r = new R();
        r.setCode(code.LOGIN_OK);
        r.setMsg("登录成功");

        User user =(User) authentication.getPrincipal();
        //避免暴露密码
        user.setPassword(null);
        r.setData(user);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        String s = new ObjectMapper().writeValueAsString(r);
        response.getWriter().println(s);
    }
}
