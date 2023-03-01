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

//    @Select("select *  from user")
//    public Collection<User> SelectUserByUsername();

    public Collection<User> ListUserByProject(int projectid);
}
