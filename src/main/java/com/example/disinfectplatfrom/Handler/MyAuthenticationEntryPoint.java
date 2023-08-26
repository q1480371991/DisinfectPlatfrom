package com.example.disinfectplatfrom.Handler;

import com.example.disinfectplatfrom.Utils.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author : Lin
 * @version : [v1.0]
 * @className : MyAuthenticationEntryPoint
 * @description : 用来解决未认证用户访问无权限资源时的异常
 * @createTime : 2023/6/19 18:38
 * @updateUser : Lin
 * @updateTime : 2023/6/19 18:38
 * @updateRemark :
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
 
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        R r = new R();
        r.setMsg("请认证之后再去处理");
        r.setCode(HttpStatus.UNAUTHORIZED.value());
        String errormsg = objectMapper.writeValueAsString(r);
        out.write(errormsg);
        out.flush();
        out.close();
    }
}