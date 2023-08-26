package com.example.disinfectplatfrom.Controller;

import com.example.disinfectplatfrom.Pojo.Authority;
import com.example.disinfectplatfrom.Pojo.Role;
import com.example.disinfectplatfrom.Pojo.User;
import com.example.disinfectplatfrom.Service.UserService;
import com.example.disinfectplatfrom.Utils.R;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.deploy.net.HttpRequest;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

//@PreAuthorize("hasRole('OA')")
@RestController
public class TestController {
    @Autowired
    UserService userService;
    @RequestMapping(value = "test")
//    @PreAuthorize("hasAuthority('data_analysis')")
    public R test(@RequestBody Map<String,Object> data) throws IOException {
        System.out.println(data);
        Object user = data.get("user");
        System.out.println(user.getClass());
        System.out.println(user);
        ObjectMapper mapper = new ObjectMapper();
        User user1 = mapper.convertValue(user, User.class);
        System.out.println(user1);
        System.out.println(user1.getClass());


        return new R("true");
    }
//    @PreAuthorize("hasRole('HW')")
    @RequestMapping(value = "test1",method = RequestMethod.POST)
    public R test1(@RequestBody String data) throws IOException {
        return R.ok(null);
    }

    //
    @RequestMapping(value = "test2")
    public Collection GetRolesByOrgnizationid(Integer orgnizationid) throws IOException {
        Collection<Map<String, Object>> res = new ArrayList<>();
        Collection<Role> roles = userService.ListRolesByProjectId(1);
        for (Role role : roles) {
            HashMap hashMap = new HashMap();
            hashMap.put("project_roleid",role.getId());
            hashMap.put("role_name",role.getRemark());
            res.add(hashMap);
        }
        return res;
    }

}
