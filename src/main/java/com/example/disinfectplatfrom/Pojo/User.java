package com.example.disinfectplatfrom.Pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
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
//@TableName(value = "user")
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
    private Timestamp createTime;
    private Timestamp updateTime;
    private int delFlag;
    private String remark;
    @TableField(exist = false)
    private Collection<String> roles;//关系属性 用来存储当前用户所有角色信息
    @TableField(exist = false)
    private Collection<Authority> authorities;
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
        if(!ObjectUtils.isEmpty(this.authorities) && !ObjectUtils.isEmpty(this.roles)){
            Collection<GrantedAuthority> authoritylist = new ArrayList<GrantedAuthority>();
            for (Authority authority:this.authorities
            ) {
                if(!ObjectUtils.isEmpty(authority)){
                    //没写完
                    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority.getAuthorityName());
                    authoritylist.add(simpleGrantedAuthority);
                }
            }
            for (String role : this.roles) {
                if(!ObjectUtils.isEmpty(role)){
                    //没写完
                    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role);
                    authoritylist.add(simpleGrantedAuthority);
                }
            }
            return authoritylist;
        }else{
            return null;
        }

    }

    public void setRoles(Collection<Role> roles)  {
        if(!ObjectUtils.isEmpty(roles)){
            Collection<String> myroles= new ArrayList<String>();
            for (Role role : roles) {
                if (!ObjectUtils.isEmpty(role)){
                    myroles.add(role.getRoleName());
                }
            }
            this.roles=myroles;
        }else{
            System.out.println("Roles不存在");
        }
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

    @JsonIgnore
    public Boolean isHW(){
        String hw="ROLE_HW";
        for (String role : this.roles) {
            if (hw.equals(role)){
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public Boolean isOA(){
        String oa="ROLE_OA";
        for (String role : this.roles) {
            if (oa.equals(role)){
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public Boolean isPA(){
        String pa="ROLE_PA";
        for (String role : this.roles) {
            if (pa.equals(role)){
                return true;
            }
        }
        return false;
    }
}
