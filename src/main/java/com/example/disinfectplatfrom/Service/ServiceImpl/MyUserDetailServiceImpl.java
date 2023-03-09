package com.example.disinfectplatfrom.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.disinfectplatfrom.Mapper.UserMapper;
import com.example.disinfectplatfrom.Pojo.User;
import com.example.disinfectplatfrom.Service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class MyUserDetailServiceImpl implements MyUserDetailService {
    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户的基本信息
        User user = userMapper.GetUserByUsername(username);
        if(!ObjectUtils.isEmpty(user)){
            throw new UsernameNotFoundException("当前用户不存在");
        }
        //查询用户的roles字符串

        //查询用户的authority字符串
        return user;
    }


}
