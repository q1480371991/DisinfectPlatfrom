package com.example.disinfectplatfrom.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.disinfectplatfrom.Pojo.Orgnization;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrgnizationMapper extends BaseMapper<Orgnization> {

    @Insert("INSERT INTO orgnization_user VALUES(NULL,#{userid},#{projectid})")
    public void AddOrgnization_User(Integer userid,Integer orgnizationid);

    @Insert("INSERT INT orgnization_project VALUES(NULL,#{orgnizationid},#{projectid},0)")
    public void AddOrgnization_Project(Integer orgnizationid,Integer projectid);
}
