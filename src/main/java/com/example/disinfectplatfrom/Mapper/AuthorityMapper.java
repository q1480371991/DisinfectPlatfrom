package com.example.disinfectplatfrom.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.disinfectplatfrom.Pojo.Authority;
import com.example.disinfectplatfrom.Pojo.Role;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@Mapper
public interface AuthorityMapper extends BaseMapper<Authority> {
    @Select("SELECT * FROM menu WHERE id IN (SELECT menu_id FROM role_menu WHERE role_id IN (SELECT id FROM role WHERE id IN (SELECT role_id FROM user_role WHERE user_id = #{userid})))")
    public Collection<Authority> ListAuthoritiesByUserId(int userid);
    @Insert("INSERT role_menu VALUES(NULL ,#{roleid},#{menuid})")
    public void AddRole_Menu(Integer roleid,Integer menuid);
    @Select("SELECT * FROM menu WHERE id IN (SELECT menu_id FROM role_menu WHERE role_id=#{roleid} )")
    public Collection<Authority> ListAuthoritiesByRoleId(int roleid);
}
