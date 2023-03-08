package com.example.disinfectplatfrom;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.disinfectplatfrom.Controller.TestController;
import com.example.disinfectplatfrom.Mapper.ProjectMapper;
import com.example.disinfectplatfrom.Mapper.UserMapper;
import com.example.disinfectplatfrom.Pojo.Project;
import com.example.disinfectplatfrom.Pojo.User;
import com.example.disinfectplatfrom.Service.ServiceImpl.MyUserDetailServiceImpl;
import com.example.disinfectplatfrom.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

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
    @Test
    public void test1(){
        LambdaQueryWrapper<Project> lqw = new LambdaQueryWrapper<Project>();
        lqw.eq(Project::getProjectid,3);
        System.out.println(projectMapper.selectOne(lqw));
    }
}
