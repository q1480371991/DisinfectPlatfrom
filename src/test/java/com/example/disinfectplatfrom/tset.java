package com.example.disinfectplatfrom;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.disinfectplatfrom.Mapper.AuthorityMapper;
import com.example.disinfectplatfrom.Mapper.ProjectMapper;
import com.example.disinfectplatfrom.Mapper.RoleMapper;
import com.example.disinfectplatfrom.Mapper.UserMapper;
import com.example.disinfectplatfrom.Pojo.Role;
import com.example.disinfectplatfrom.Service.ServiceImpl.MyUserDetailServiceImpl;
import com.example.disinfectplatfrom.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    AuthorityMapper authorityMapper;
    @Test
    public void test1(){
//        Role role = new Role();
//        role.setRoleName("test");
//        role.setDelFlag(0);
//        role.setRemark("test1");
//        Date date = new Date();
//        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
//        String format = dateFormat.format(date);
//        System.out.println(format);
//        role.setUpdateTime(format);
//        roleMapper.insert(role);
        List<Role> roles = roleMapper.selectList(new QueryWrapper<>());
        System.out.println(roles);
    }
}
