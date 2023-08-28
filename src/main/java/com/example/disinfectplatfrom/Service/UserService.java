package com.example.disinfectplatfrom.Service;

import com.example.disinfectplatfrom.Pojo.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserService {

    //返回所有的账号信息
    public Collection<User> ListAllUser();//海威账号功能

    //修改密码
    public void UpdatePassword(String password);//通过id修改用户密码

    //修改手机号
    public void UpdatePhonenumber(String phonenumber);//通过id修改手机号
    //修改邮箱
    public void UpdateEmail(String email);//通过id修改邮箱

    //返回项目下所有账户内容  仅项目管理员
    public Collection<User> ListUserByProjectId(Integer projectid,Integer flag);

    //返回项目管理员所管理的项目下所有账户内容    仅项目管理员
    public Collection<User> ListUserByProjectAdminId();

    //返回项目下所有账户内容    仅项目管理员 or HW
    //海威顶级账号查看数据范围：所有项目的所有管理台账户内容。管理员查看数据范围：项目内所有管理台账户内容。
    public Collection<User> ListUsers();


    //返回所有项目的信息，仅海威账号
    public Collection<Project> ListAllProject();

    //选择项目管理员管理的项目，仅海威账号
    public void UpdataProjectAdmin(Integer projectid,Integer adminid);

    //创建项目初始账号，仅海威账号
    public void AddProjectOriginAccount(Integer projectid,User user);

    Project GetCurrentProject();

    //返回项目管理员下的项目信息
    public Collection<Project> ListProjectsByAdminid(Integer adminid);

    //添加项目，仅海威账号
    public Boolean AddProject(Project project);
    //编辑项目，仅海威账号
    public void UpdateProjectById(int id,String projectname,String remark);

    //返回组织下的所有账号   仅组织管理员
    public Collection<User> ListUserByOrgnizationId(int id);

    //添加组织管理员，仅限海威账号
    public void AddProjectAdmin(User user);

    //小程序用户注册
    public void AddSmallRoutineUser(User user,Integer orgnizationid);

    //添加项目用户，仅限项目管理员
    public void AddProjectUser(User user,Integer projectid,ArrayList<Integer> roleids);

    //添加组织 仅项目管理员
    public void AddOrganization(Orgnization orgnization);

    //往项目中添加组织 仅项目管理员
    public void AddOrganization_Project(Integer orgnizationid,Integer projectid);

    //项目管理员查询所管理项目下的组织    仅项目管理员
    public ArrayList<Orgnization> ListOrgnizationByProjectid(Integer projectid);

    //查询用户
    public Collection<User> SelectUser(String username, Integer projectid,Integer status,String email,String phonenumber);



    //检测用户账户名是否重复
    public boolean CheckUserName(String username);

    //新增角色
    public void AddRole(Role role, Integer projectid, List<Integer> authorities,Integer quantity,List<Integer> orgnizations);

    //查询项目下的角色
    public ArrayList ListRolesByProjectId(Integer projectid);


}
