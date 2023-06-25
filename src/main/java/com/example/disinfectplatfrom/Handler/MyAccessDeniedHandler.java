package com.example.disinfectplatfrom.Handler;

import com.example.disinfectplatfrom.Utils.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author : Lin
 * @version : [v1.0]
 * @className : MyAccessDeniedHandler
 * @description : 用来解决认证过的用户访问无权限资源时的异常
 * @createTime : 2023/6/19 18:38
 * @updateUser : Lin
 * @updateTime : 2023/6/19 18:38
 * @updateRemark :
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {


        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        R r = new R();
        r.setMsg("权限不足");
        r.setCode(403);
        String errorMsg = objectMapper.writeValueAsString(r);
        out.write(errorMsg);
        out.flush();
        out.close();
    }
}