package com.example.disinfectplatfrom.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.disinfectplatfrom.Mapper.OrgnizationMapper;
import com.example.disinfectplatfrom.Mapper.ProjectMapper;
import com.example.disinfectplatfrom.Mapper.ProjectUserMapper;
import com.example.disinfectplatfrom.Mapper.UserMapper;
import com.example.disinfectplatfrom.Pojo.Project;
import com.example.disinfectplatfrom.Pojo.ProjectUser;
import com.example.disinfectplatfrom.Pojo.User;
import com.example.disinfectplatfrom.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private OrgnizationMapper orgnizationMapper;
    @Autowired
    private ProjectUserMapper project_userMapper;

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
        queryWrapper.select(User.class, info ->!info.getColumn().equals("password"));//查询指定某字段以外的数据

        List<User> Users=userMapper.selectList(queryWrapper);

        Users.set(0, null);//不包括海威账号
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
//        System.out.println(currentUser);
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
     * @Param :[projectid]
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.User>
     **/
    @Override
    public Collection<User> ListUserByProjectId(int projectid) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User currentUser = (User)authentication.getPrincipal();
        LambdaQueryWrapper<User> userlqw = new LambdaQueryWrapper<User>();
        LambdaQueryWrapper<ProjectUser> project_userlqw = new LambdaQueryWrapper<ProjectUser>();
        project_userlqw.select(ProjectUser::getUserid).eq(ProjectUser::getProjectid,projectid);
        Collection<ProjectUser> userids = project_userMapper.selectList(project_userlqw);
        Collection c=new ArrayList();
        userids.addAll(c);
        userids.forEach(new Consumer<ProjectUser>() {
            @Override
            public void accept(ProjectUser projectUser) {
                userlqw.or().eq(User::getId,projectUser.getUserid());
            }
        });
        List<User> users = userMapper.selectList(userlqw);
//        System.out.println(users);
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
    @Override
    public Collection<User> ListUserByProjectAdminId() {
        Collection<User> res=new ArrayList<User>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)authentication.getPrincipal();
        LambdaQueryWrapper<Project> lqw = new LambdaQueryWrapper<Project>();
        lqw.select(Project::getId).eq(Project::getAdminid,currentUser.getId());
        Collection<Project> projects = projectMapper.selectList(lqw);
        for (Project project : projects) {
            Collection<User> users = ListUserByProjectId(project.getId());
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
        List<Project> projects = projectMapper.selectList(new LambdaQueryWrapper<Project>());
//        System.out.println(projects);
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
    public Collection<User> ListUserByOrgnizationId(int id) {
        return null;
    }

}
