package com.example.disinfectplatfrom.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.disinfectplatfrom.Mapper.OrgnizationMapper;
import com.example.disinfectplatfrom.Mapper.ProjectMapper;
import com.example.disinfectplatfrom.Mapper.UserMapper;
import com.example.disinfectplatfrom.Pojo.Project;
import com.example.disinfectplatfrom.Pojo.User;
import com.example.disinfectplatfrom.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author : Lin
 * @version : [v1.0]
 * @className : UserServiceImp
 * @description : UserService
 * @createTime : 2023/3/1 22:53
 * @updateUser : Lin
 * @updateTime : 2023/3/1 22:53
 * @updateRemark : 描述说明本次修改内容
 */
@SuppressWarnings({"ALL", "AlibabaClassMustHaveAuthor"})
@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private OrgnizationMapper orgnizationMapper;

    /*
     * @title :ListAllUser
     * @Author :Lin
     * @Description : 获取所有账号信息，仅限海威账号
     * @Date :22:39 2023/3/1
     * @Param :[]
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.User>
     **/
    @Override
    public Collection<User> ListAllUser()
    {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        //查询指定某字段以外的数据
        queryWrapper.select(User.class, info ->!info.getColumn().equals("password"));
        List<User> Users=userMapper.selectList(queryWrapper);

        //不包括海威账号
        Users.set(0, null);
        return Users;
    }

    /*
     * @title :UpdatePassword
     * @Author :Lin
     * @Description : 修改账号密码
     * @Date :22:40 2023/3/1
     * @Param :[password]
     * @return :void
     **/
    @Override
    public void UpdatePassword(String password) {
        //获取当前用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)authentication.getPrincipal();
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        //直接修改当前用户的密码，不需要前端传用户id
        User user = userMapper.selectById(currentUser.getId());
        password="{noop}"+password;
        user.setPassword(password);
        userMapper.updateById(user);
    }

    /*
     * @title :ListUserByProjectId
     * @Author :Lin
     * @Description : 获取项目下所有账号信息
     * @Date :22:40 2023/3/1
     * @Param :[projectid] [flag]:0查询的List包括项目管理员和初始账号，1不包括
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.User>
     **/
    @Override
    public Collection<User> ListUserByProjectId(Integer projectid, Integer flag) {
        List<User> users;
        LambdaQueryWrapper<User> userlqw = new LambdaQueryWrapper<User>();
        Collection<Integer> userids = userMapper.ListUserIdInProjectById(projectid);
        if (flag==1){
            Project project = projectMapper.selectById(projectid);
            Collection<Integer> arr = new ArrayList<>();
            arr.add(project.getAdminid());
            arr.add(project.getOriginaccountid());
            userids.removeAll(arr);
        }
        if(!userids.isEmpty())
        {
            userlqw.in(User::getId,userids);
            users = userMapper.selectList(userlqw);
        }
        else {
            users=null;
        }

        return users;
    }

    /*
     * @title :ListUserByProjectAdminId
     * @Author :Lin
     * @Description : 获取项目管理员所有项目下的账号信息
     * @Date :22:41 2023/3/1
     * @Param :[]
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.User>
     **/
    @PreAuthorize("hasRole()")
    @Override
    public Collection<User> ListUserByProjectAdminId() {
        Collection<User> res=new ArrayList<User>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)authentication.getPrincipal();
        LambdaQueryWrapper<Project> lqw = new LambdaQueryWrapper<Project>();
        lqw.select(Project::getId).eq(Project::getAdminid,currentUser.getId());
        Collection<Project> projects = projectMapper.selectList(lqw);
        for (Project project : projects) {
            Collection<User> users = ListUserByProjectId(project.getId(),0);
            res.addAll(users);
        }
        return res;
    }

    /*
     * @title :ListAllProject
     * @Author :Lin
     * @Description : 获取所有项目信息
     * @Date :22:41 2023/3/1
     * @Param :[]
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.Project>
     **/
    @Override
    public Collection<Project> ListAllProject() {
        LambdaQueryWrapper<Project> lqw = new LambdaQueryWrapper<>();
        //查没有被逻辑删除的项目
        lqw.eq(Project::getDel_flag,0);
        List<Project> projects = projectMapper.selectList(lqw);
        return projects;
    }

    /*
     * @title :AddProject
     * @Author :Lin
     * @Description : 新增项目，仅限海威账号
     * @Date :22:42 2023/3/1
     * @Param :[project]
     * @return :void
     **/
    @Override
    public void AddProject(Project project) {
        projectMapper.insert(project);
    }

    /*
     * @title :UpdateProjectId
     * @Author :Lin
     * @Description : 更新项目
     * @Date :22:42 2023/3/1
     * @Param :[id, projectname, remark]
     * @return :void
     **/
    @Override
    public void UpdateProjectById(int id, String projectname, String remark) {
        Project project = projectMapper.selectById(id);
        project.setProjectname(projectname);
        project.setRemark(remark);
        projectMapper.updateById(project);
    }

    /*
     * @title :ListUserByOrgnizationId
     * @Author :Lin
     * @Description : 获取组织下的所有账号信息
     * @Date :22:43 2023/3/1
     * @Param :[id]
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.User>
     **/
    @Override
    public Collection<User> ListUserByOrgnizationId(int projectid) {
        Collection<Integer> userids = userMapper.ListUserIdInOrgnizationById(projectid);
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        lqw.in(User::getId,userids);
        List<User> users = userMapper.selectList(lqw);
        return users;
    }
    /*
     * @title :AddProjectAdmin
     * @Author :Lin
     * @Description : 添加组织管理员，仅限海威账号
     * @Date :22:56 2023/3/6
     * @Param :[user]
     * @return :void
     **/
    @Override
    public void AddProjectAdmin(User user) {
        userMapper.insert(user);
    }

    /*
     * @title :SelectUser
     * @Author :Lin
     * @Description : 模糊查询用户
     * @Date :16:55 2023/3/7
     * @Param :[s, projectid]
     * @return :void
     **/
    @Override
    public Collection<User> SelectUser(String s, Integer projectid,Integer status,String email,String phonenumber) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        if(!ObjectUtils.isEmpty(status)){
            lqw.eq(User::getStatus,status);}
        if(!ObjectUtils.isEmpty(email)) {
            lqw.eq(User::getEmail, email);
        }
        if(!ObjectUtils.isEmpty(phonenumber)) {
            lqw.eq(User::getPhonenumber, phonenumber);
        }
        if(!ObjectUtils.isEmpty(projectid)){
            Collection<Integer> userid = userMapper.ListUserIdInProjectById(projectid);
            lqw.in(User::getId,userid);
        }
        if(!ObjectUtils.isEmpty(s)) {
            lqw.like(User::getName, s);
        }
        List<User> users = userMapper.selectList(lqw);
        return users;
    }

    /*
     * @title :AddOrginationUser
     * @Author :Lin
     * @Description : 添加组织用户，仅限项目管理员
     * @Date :23:05 2023/3/6
     * @Param :[user]
     * @return :void
     **/
    @Override
    public void AddOrginationUser(User user) {

    }

    /*
     * @title :AddSmallRoutineUser
     * @Author :Lin
     * @Description : 小程序用户注册
     * @Date :22:57 2023/3/6
     * @Param :[user]
     * @return :void
     **/
    @Override
    public void AddSmallRoutineUser(User user) {
        userMapper.insert(user);
    }

    /*
     * @title :CheckProjectName
     * @Author :Lin
     * @Description : 检测用户账户名是否重复
     * @Date :21:34 2023/3/7
     * @Param :[projectname]
     * @return :boolean
     **/
    @Override
    public boolean CheckUserName(String username) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        lqw.eq(User::getUsername,username);
        lqw.eq(User::getDel_flag,0);
        User user = userMapper.selectOne(lqw);
        if(!ObjectUtils.isEmpty(user)){
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void AddRole() {

    }

}
