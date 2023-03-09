package com.example.disinfectplatfrom.Pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
/**
 * @author : Lin
 * @version : [v1.0]
 * @className : User
 * @description : POJO
 * @createTime : 2023/3/1 22:53
 * @updateUser : Lin
 * @updateTime : 2023/3/1 22:53
 * @updateRemark : 描述说明本次修改内容
 */
@Data
public class User implements UserDetails {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField(condition = SqlCondition.LIKE)
    private String name;
    private String username;
    @TableField(select = false)
    private String password;
    private int status;
    private String email;
    private String phonenumber;
    private int sex;
    private String createtime;
    private String updatetime;
    private int del_flag;
    private String remark;
    @TableField(exist = false)
    private Collection<Role> roles;//关系属性 用来存储当前用户所有角色信息
    @TableField(exist = false)
    private Collection<String> authorities;
    private int isfirst;

    /*
     * @title :getAuthorities
     * @Author :Lin
     * @Description : 返回用户的权限字符串集合，没写完
     * @Date :23:06 2023/3/9
     * @Param :[]
     * @return :java.util.Collection<? extends org.springframework.security.core.GrantedAuthority>
     **/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authoritylist = new ArrayList<>();
        for (String authority:this.authorities
             ) {
            if(!ObjectUtils.isEmpty(authority)){
                //没写完
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
                authoritylist.add(simpleGrantedAuthority);
            }
        }
        return authoritylist;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
