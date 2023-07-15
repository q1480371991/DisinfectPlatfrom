package com.example.disinfectplatfrom.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.disinfectplatfrom.Pojo.Orgnization;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Mapper
@Repository
public interface OrgnizationMapper extends BaseMapper<Orgnization> {

    @Insert("INSERT INTO orgnization_user VALUES(NULL,#{userid},#{projectid})")
    public void AddOrgnization_User(Integer userid,Integer orgnizationid);

    @Insert("INSERT INTO orgnization_project VALUES(NULL,#{orgnizationid},#{projectid},0)")
    public void AddOrgnization_Project(Integer orgnizationid,Integer projectid);

    @Select("SELECT * FROM orgnization WHERE `name` LIKE CONCAT ('%',#{name},'%') AND phonenumber LIKE CONCAT ('%',#{phonenum},'%')")
    public Collection<Orgnization> SelectOrgnization(String name,String phonenum);
}
