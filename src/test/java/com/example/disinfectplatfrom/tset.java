package com.example.disinfectplatfrom;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.disinfectplatfrom.Mapper.*;
import com.example.disinfectplatfrom.Pojo.*;
import com.example.disinfectplatfrom.Service.DeviceService;
import com.example.disinfectplatfrom.Service.ProjectService;
import com.example.disinfectplatfrom.Service.ServiceImpl.MyUserDetailServiceImpl;
import com.example.disinfectplatfrom.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
public class tset {
    @Autowired
    MyUserDetailServiceImpl myUserDetailService;
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    AuthorityMapper authorityMapper;
    @Autowired
    DeviceService deviceService;
    @Autowired
    ProjectService projectService;
    @Autowired
    OrgnizationMapper orgnizationMapper;
    @Test
    public void test1(){
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        lqw.eq(User::getId,1);
        lqw.select(User.class, info -> !info.getColumn().equals("password"));
        User user = userMapper.selectOne(lqw);
        System.out.println(user);

    }
}
