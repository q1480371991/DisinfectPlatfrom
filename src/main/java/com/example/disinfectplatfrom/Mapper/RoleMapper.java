package com.example.disinfectplatfrom.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.disinfectplatfrom.Pojo.Role;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

@Mapper
@Repository
public interface RoleMapper extends BaseMapper<Role> {
    @Select("SELECT * FROM role WHERE id IN (SELECT role_id FROM user_role WHERE user_id = #{userid})")
    public Collection<Role> ListRolesByUserId(int userid);

    @Insert("INSERT INTO role_orgnization VALUES(NULL ,#{roleid},#{orgnizationid})")
    public void AddRole_Orgnization(Integer roleid,Integer orgnizationid);

    public Collection<Map<Integer,String>>ListRolesByOrgnizationId(Integer orgnizationid);
}
