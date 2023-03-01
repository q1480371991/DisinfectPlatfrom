package com.example.disinfectplatfrom.Service;

import com.example.disinfectplatfrom.Pojo.Project;
import com.example.disinfectplatfrom.Pojo.User;
import org.apache.ibatis.annotations.Update;

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
    public void UpdateProject(int id,String projectname,String remark);
}
