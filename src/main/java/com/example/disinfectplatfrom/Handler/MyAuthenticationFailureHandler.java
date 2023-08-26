package com.example.disinfectplatfrom.Handler;

import com.example.disinfectplatfrom.Utils.R;
import com.example.disinfectplatfrom.Utils.code;
import com.example.disinfectplatfrom.exception.KaptchaNotMatchException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
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
 * @className : MyAuthenticationFailureHandler
 * @description : 认证失败处理器
 * @createTime : 2023/6/19 18:42
 * @updateUser : Lin
 * @updateTime : 2023/6/19 18:42
 * @updateRemark : 认证失败处理器
 */


@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        R r = new R();
        //        KaptchaNotMatchException  BadCredentialsException
        if (exception instanceof BadCredentialsException){
            //账号不存在 or 密码错误
            r.setCode(code.LOGIN_ACERR_PWDERR);
        }
        if (exception instanceof KaptchaNotMatchException){
            // 验证码错误
            r.setCode(code.LOGIN_VCERR);
        }
        r.setMsg("登录失败 "+exception.getMessage());
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        String s = new ObjectMapper().writeValueAsString(r);
        response.getWriter().println(s);
    }
}
