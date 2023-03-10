package com.example.disinfectplatfrom.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.disinfectplatfrom.Mapper.AuthorityMapper;
import com.example.disinfectplatfrom.Mapper.RoleMapper;
import com.example.disinfectplatfrom.Mapper.UserMapper;
import com.example.disinfectplatfrom.Pojo.Authority;
import com.example.disinfectplatfrom.Pojo.Role;
import com.example.disinfectplatfrom.Pojo.User;
import com.example.disinfectplatfrom.Service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collection;

@Service
public class MyUserDetailServiceImpl implements MyUserDetailService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AuthorityMapper authorityMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户的基本信息
        User user = userMapper.GetUserByUsername(username);
        if(ObjectUtils.isEmpty(user)){
            throw new UsernameNotFoundException("当前用户不存在");
        }
        //查询用户的roles字符串
        Collection<Role> roles = roleMapper.ListRolesByUserId(user.getId());
        user.setRoles(roles);
        //查询用户的authority字符串
        Collection<Authority> authorities = authorityMapper.ListAuthoritiesByUserId(user.getId());
        user.setAuthorities(authorities);

        return user;
    }


}
