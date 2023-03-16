package com.example.disinfectplatfrom.Service;

import com.example.disinfectplatfrom.Pojo.Project;
import com.example.disinfectplatfrom.Pojo.Role;
import com.example.disinfectplatfrom.Pojo.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserService {

    public Collection<User> ListAllUser();//海威账号功能

//    @Update("update user set password=#{password} where id=#{id}")
    public void UpdatePassword(String password);//通过id修改用户密码

    //返回项目下所有账户内容
    public Collection<User> ListUserByProjectId(Integer projectid,Integer flag);

    //返回项目管理员下所有账户内容
    public Collection<User> ListUserByProjectAdminId();

    //返回所有项目的信息，仅海威账号
    public Collection<Project> ListAllProject();
    //添加项目，仅海威账号
    public void AddProject(Project project);
    //编辑项目，仅海威账号
    public void UpdateProjectById(int id,String projectname,String remark);

    //返回组织下的用户账号
    public Collection<User> ListUserByOrgnizationId(int id);

    //添加组织管理员，仅限海威账号
    public void AddProjectAdmin(User user);

    //小程序用户注册
    public void AddSmallRoutineUser(User user,Integer orgnizationid);

    //添加项目用户，仅限项目管理员
    public void AddProjectUser(User user,Integer projectid,Integer roleid);

    //查询用户
    public Collection<User> SelectUser(String s, Integer projectid,Integer status,String email,String phonenumber);

    //检测用户账户名是否重复
    public boolean CheckUserName(String username);

    //新增角色
    public void AddRole(Role role, Integer projectid, List<Integer> authorities,Integer quantity,List<Integer> orgnizations);

    //查询项目下的角色
    public Collection<Role> ListRolesByProjectId(Integer projectid);
}
