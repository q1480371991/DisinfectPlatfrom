package com.example.disinfectplatfrom.Service;

import com.example.disinfectplatfrom.Pojo.Project;
import com.example.disinfectplatfrom.Pojo.User;

import java.util.Collection;

public interface UserService {

    public Collection<User> ListAllUser();//海威账号功能

//    @Update("update user set password=#{password} where id=#{id}")
    public void UpdatePassword(String password);//通过id修改用户密码

    //返回项目下所有账户内容
    public Collection<User> ListUserByProjectId(int projectid);

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
    public void AddSmallRoutineUser(User user);

    //添加组织用户，仅限项目管理员
    public void AddOrginationUser(User user);
}
