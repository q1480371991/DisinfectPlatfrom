package com.example.disinfectplatfrom.session;

import com.example.disinfectplatfrom.Pojo.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qilong.sun
 * @version v1.0
 * @time 2021/10/27 9:49
 * @title
 * @description
 */
@Component
public class MySessionRegistryImpl extends SessionRegistryImpl {
    @Override
    public List<SessionInformation> getAllSessions(Object principal, boolean includeExpiredSessions) {
        User user= (User) principal;
        String username = ((UserDetails) principal).getUsername();
        return super.getAllSessions(user, includeExpiredSessions);
    }
}
