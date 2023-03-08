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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//@SpringBootTest
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
        Collection<Integer> s1 = new ArrayList<>();
        Collection<Integer> s2 = new ArrayList<>();
        for(int i=0;i<10;i++)
        {
            if(i>5)s2.add(i);
            s1.add(i);
        }
        s1.removeAll(s2);

        System.out.println(s1);
    }
}
