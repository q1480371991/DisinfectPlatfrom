package com.example.disinfectplatfrom.Controller;

import com.example.disinfectplatfrom.Pojo.User;
import com.example.disinfectplatfrom.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.deploy.net.HttpRequest;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@RestController
public class TestController {
    @Autowired
    UserService userService;
    @RequestMapping(value = "test")
    public String test(HttpServletRequest request) throws IOException {
        Map<String, Object> userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);
        System.out.println(userInfo.get("s1").getClass());

        return "test";
    }

    @RequestMapping(value = "test1")
    public boolean test1(@RequestBody Map<String,Object> info) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)authentication.getPrincipal();
        System.out.println(currentUser.getAuthorities());
        return true;
    }

}
