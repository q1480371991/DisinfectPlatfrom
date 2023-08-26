package com.example.disinfectplatfrom.Handler;

import com.example.disinfectplatfrom.Utils.R;
import com.example.disinfectplatfrom.Utils.code;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
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
 * @className : MyLogoutSuccessHandler
 * @description : 描述说明该类的功能
 * @createTime : 2023/7/17 22:45
 * @updateUser : Lin
 * @updateTime : 2023/7/17 22:45
 * @updateRemark : 描述说明本次修改内容
 */
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        R r = new R();
        System.out.println("注销成功");
        r.setCode(code.LOGOUT_OK);
        r.setData(authentication.getPrincipal());
        r.setMsg("注销成功");
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        String s = new ObjectMapper().writeValueAsString(r);
        response.getWriter().println(s);
    }
}
