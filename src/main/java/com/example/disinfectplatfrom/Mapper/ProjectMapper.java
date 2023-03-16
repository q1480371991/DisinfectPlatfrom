package com.example.disinfectplatfrom.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.disinfectplatfrom.Pojo.Project;
import com.example.disinfectplatfrom.Pojo.Project_Role;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Mapper
@Repository
public interface ProjectMapper extends BaseMapper<Project> {
    @Insert("INSERT INTO project_roles VALUES(NULL ,#{projectid},#{roleid},#{maxAccount},0)")
    public void AddProject_Roles(Integer projectid,Integer roleid,Integer maxAccount);

    @Insert("INSERT INTO project_user VALUES(NULL,#{projectid},#{userid})")
    public void AddProject_User(Integer projectid,Integer userid);

    @Update("UPDATE project_roles SET current_account=#{currentaccount} WHERE role_id=#{roleid} AND project_id=#{projectid}")
    public void UpdateProjectRoleAccount(Integer currentaccount,Integer projectid,Integer roleid);

    @Select("SELECT * FROM project_roles WHERE project_id=#{projectid}")
    public Collection<Project_Role> ListProject_RoleByProjectid(Integer projectid);

    @Select("SELECT * FROM project_roles WHERE project_id=#{projectid} AND role_id=#{roleid}")
    public Project_Role SelectProject_Role(Integer projectid, Integer roleid);

}
