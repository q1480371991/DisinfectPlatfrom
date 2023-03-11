package com.example.disinfectplatfrom.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.disinfectplatfrom.Pojo.Project;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ProjectMapper extends BaseMapper<Project> {
    @Insert("INSERT INTO project_roles VALUES(NULL ,#{projectid},#{roleid},#{maxAccount},0)")
    public void AddProject_Roles(Integer projectid,Integer roleid,Integer maxAccount);
    @Update("UPDATE project_roles SET current_account=#{quantity} WHERE role_id=#{roleid} AND project_id=#{projectid}")
    public void UpdateRoleAccount(Integer quantity,Integer projectid,Integer roleid);
}
