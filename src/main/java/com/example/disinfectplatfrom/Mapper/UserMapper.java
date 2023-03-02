package com.example.disinfectplatfrom.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.disinfectplatfrom.Pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {


    @Select("SELECT userid FROM ` orgnization_user` WHERE orgnizationid =#{orgnizationid}")
    public Collection<Integer> ListUserIdInOrgnizationById(int orgnizationid);
    @Select("SELECT userid FROM project_user WHERE projectid=#{projectid}")
    public Collection<Integer> ListUserIdInProjectById(int projectid);
}
