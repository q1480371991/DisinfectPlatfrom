package com.example.disinfectplatfrom;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.disinfectplatfrom.Mapper.AuthorityMapper;
import com.example.disinfectplatfrom.Mapper.ProjectMapper;
import com.example.disinfectplatfrom.Mapper.RoleMapper;
import com.example.disinfectplatfrom.Mapper.UserMapper;
import com.example.disinfectplatfrom.Pojo.*;
import com.example.disinfectplatfrom.Service.DeviceService;
import com.example.disinfectplatfrom.Service.ProjectService;
import com.example.disinfectplatfrom.Service.ServiceImpl.MyUserDetailServiceImpl;
import com.example.disinfectplatfrom.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    @Test
    public void test1(){
        Role role = new Role(null, "q", "0", 0, "2023-07-14 21:20:09", "2023-07-14 21:20:09");
        int insert = roleMapper.insert(role);
        System.out.println(role.getId());

    }
}
