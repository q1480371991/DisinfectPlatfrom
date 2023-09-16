package com.example.disinfectplatfrom.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author : Lin
 * @version : [v1.0]
 * @className : CustomExpiredSessionStrategy
 * @description : 描述说明该类的功能
 * @createTime : 2023/9/16 12:09
 * @updateUser : Lin
 * @updateTime : 2023/9/16 12:09
 * @updateRemark : 描述说明本次修改内容
 */
public class CustomExpiredSessionStrategy implements SessionInformationExpiredStrategy {

    //页面跳转的处理逻辑
    private RedirectStrategy redirectStrategy =new DefaultRedirectStrategy();

    private ObjectMapper objectMapper=new ObjectMapper();

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        HashMap<String , Object> map = new HashMap<>();
        map.put("code",403);
        map.put("msg","被迫下线");

        String json = objectMapper.writeValueAsString(map);
        event.getResponse().setContentType("application/json;charset=utf-8");
        event.getResponse().getWriter().write(json);
    }
}
