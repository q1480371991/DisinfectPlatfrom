package com.example.disinfectplatfrom.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.disinfectplatfrom.Mapper.*;
import com.example.disinfectplatfrom.Pojo.*;
import com.example.disinfectplatfrom.Service.UserService;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Array;
import java.util.*;

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
@Data
@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private OrgnizationMapper orgnizationMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AuthorityMapper authorityMapper;
    @Autowired
    private DeviceMapper deviceMapper;

    /*
     * @title :ListAllUser
     * @Author :Lin
     * @Description : 获取所有账号信息，仅限海威账号
     * @Date :22:39 2023/3/1
     * @Param :[]
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.User>
     **/
    @Override
    @PreAuthorize("hasRole('HW')")
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
        User user = (User)authentication.getPrincipal();
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        //直接修改当前用户的密码，不需要前端传用户id
        password="{noop}"+password;
        user.setPassword(password);
        if (user.getIsfirst()==1){
            user.setIsfirst(0);
        }
        userMapper.updateById(user);
    }

    /*
     * @title :ListUserByProjectId
     * @Author :Lin
     * @Description : 获取项目下所有账号信息  仅项目管理员
     * @Date :22:40 2023/3/1
     * @Param :[projectid] [flag]:0查询的List包括项目管理员和初始账号，1不包括
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.User>
     **/
    @PreAuthorize("hasRole('PA')")
    @Override
    public Collection<User> ListUserByProjectId(Integer projectid, Integer flag) {
        List<User> users;
        LambdaQueryWrapper<User> userlqw = new LambdaQueryWrapper<User>();
        Collection<Integer> userids = userMapper.ListUserIdInProjectById(projectid);
        if (flag==1){
            Project project = projectMapper.selectById(projectid);
            Collection<Integer> arr = new ArrayList<>();
            arr.add(project.getAdminId());
            arr.add(project.getOriginAccountId());
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
     * @Description : 获取项目管理员所有项目下的账号信息   待改，项目管理员只管理一个项目
     * @Date :22:41 2023/3/1
     * @Param :[]
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.User>
     **/
    @PreAuthorize("hasRole('PA')")
    @Override
    public Collection<User> ListUserByProjectAdminId() {
        Collection<User> res=new ArrayList<User>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)authentication.getPrincipal();
        LambdaQueryWrapper<Project> lqw = new LambdaQueryWrapper<Project>();
        //查没有被逻辑删除的项目
        lqw.eq(Project::getDelFlag,0);
        lqw.select(Project::getId).eq(Project::getAdminId,currentUser.getId());
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
     * @Description : 获取所有项目信息,仅限海威账号
     * @Date :22:41 2023/3/1
     * @Param :[]
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.Project>
     **/

    @PreAuthorize("hasRole('HW')")
    @Override
    public Collection<Project> ListAllProject() {
        LambdaQueryWrapper<Project> lqw = new LambdaQueryWrapper<>();
        //查没有被逻辑删除的项目
        lqw.eq(Project::getDelFlag,0);
        Collection<Project> projects = projectMapper.selectList(lqw);
        return projects;
    }

    /*
     * @title :ListProjectsByAdminid
     * @Author :Lin
     * @Description : 返回项目管理员下的项目信息  仅限项目管理员    未完成：一个项目管理员只能管理一个项目
     * @Date :21:09 2023/3/16
     * @Param :[projectid]
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.Project>
     **/
    @PreAuthorize("hasRole('PA')")
    @Override
    public Collection<Project> ListProjectsByAdminid(Integer adminid)
    {
        LambdaQueryWrapper<Project> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Project::getAdminId,adminid);
        //查没有被逻辑删除的项目
        lqw.eq(Project::getDelFlag,0);
        Collection<Project> projects = projectMapper.selectList(lqw);
        return projects ;
    }
    /*
     * @title :AddProject
     * @Author :Lin
     * @Description : 新增项目，仅限海威账号 待改
     * @Date :22:42 2023/3/1
     * @Param :[project]
     * @return :void
     **/
    @PreAuthorize("hasRole('HW')")
    @Override
    public Boolean AddProject(Project project) {
        int insert = projectMapper.insert(project);
        return insert==1;

    }
    /*
     * @title :AddProjectOriginAccount
     * @Author :Lin
     * @Description : 创建项目初始账号，仅海威账号
     * @Date :16:38 2023/7/14
     * @Param :[projectid, user]
     * @return :void
     **/
    @PreAuthorize("hasRole('HW')")
    @Override
    public void AddProjectOriginAccount(Integer projectid,User user){
        if (!ObjectUtils.isEmpty(user)){

            //插入user
            userMapper.insert(user);
            //个体user分配项目创始人的权限or角色
            projectMapper.AddProject_User(projectid,user.getId());
            userMapper.AddUser_Role(user.getId(),6);

            //修改项目的OriginAccountId
            LambdaQueryWrapper<Project> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Project::getProjectId,projectid);
            Project project = projectMapper.selectOne(lqw);
            project.setOriginAccountId(user.getId());
            projectMapper.updateById(project);

        }

    }



    /*
     * @title :UpdateProjectId
     * @Author :Lin
     * @Description : 更新项目
     * @Date :22:42 2023/3/1
     * @Param :[id, projectname, remark]
     * @return :void
     **/
    @PreAuthorize("hasRole('HW')")
    @Override
    public void UpdateProjectById(int projectid, String projectname, String remark) {
        Project project = projectMapper.selectById(projectid);
        project.setProjectName(projectname);
        project.setRemark(remark);
        projectMapper.updateById(project);
    }
    /*
     * @title :UpdataProjectAdmin
     * @Author :Lin
     * @Description : 选择项目管理员管理的项目，仅海威账号
     * @Date :16:32 2023/7/14
     * @Param :[adminid]
     * @return :void
     **/
    @PreAuthorize("hasRole('HW')")
    @Override
    public void UpdataProjectAdmin(Integer projectid,Integer adminid){
        LambdaQueryWrapper<Project> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Project::getProjectId,projectid);
        Project project = projectMapper.selectOne(lqw);
        project.setAdminId(adminid);
        projectMapper.updateById(project);
    }


    /*
     * @title :ListUserByOrgnizationId
     * @Author :Lin
     * @Description : 获取组织下的所有账号信息   仅组织管理员
     * @Date :22:43 2023/3/1
     * @Param :[id]
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.User>
     **/
    @Override
    @PreAuthorize("hasRole('OA')")
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
     * @Description : 添加项目管理员账号，仅限海威账号
     * @Date :22:56 2023/3/6
     * @Param :[user]
     * @return :void
     **/
    @PreAuthorize("hasRole('HW')")
    @Override
    public void AddProjectAdmin(User user) {
        //插入user
        userMapper.insert(user);
        //给user分配项目管理员的权限
        userMapper.AddUser_Role(user.getId(),3);
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
     * @title :AddProjectUser
     * @Author :Lin
     * @Description : 添加项目用户，仅限项目管理员
     * @Date :23:05 2023/3/6
     * @Param :[user]
     * @return :void
     **/
    @PreAuthorize("hasRole('PA')")
    @Override
    public void AddProjectUser(User user,Integer projectid,Integer roleid){
        //先查询该项目下该角色的信息
        Project_Role project_role = projectMapper.SelectProject_Role(projectid, roleid);
        //判断
        if (project_role.getCurrentAccount()<project_role.getMaxAccount()){
            //当前角色的数量小于最大数量，添加用户
            userMapper.insert(user);
            //将用户与项目关联
            projectMapper.AddProject_User(projectid,user.getId());
            //用户的角色
            userMapper.AddUser_Role(user.getId(),roleid);
            //更新该项目中该角色对应的current_account
            project_role.setCurrentAccount(project_role.getCurrentAccount()+1);
            projectMapper.UpdateProjectRoleAccount(project_role.getCurrentAccount(),project_role.getProjectId(),project_role.getRoleId());
        }else {
            //抛出异常
            throw new RuntimeException("当前角色数量已满");
        }
    }

    /*
     * @title :ListOrgnizationByProjectid
     * @Author :Lin
     * @Description :  项目管理员查询所管理项目下的组织    仅项目管理员
     * @Date :20:56 2023/7/14
     * @Param :[projectid]
     * @return :java.util.ArrayList<com.example.disinfectplatfrom.Pojo.Orgnization>
     **/
    @PreAuthorize("hasRole('PA')")
    @Override
    public ArrayList<Orgnization> ListOrgnizationByProjectid(Integer projectid){

        LambdaQueryWrapper<Orgnization> lqw = new LambdaQueryWrapper<Orgnization>();
        Collection<Integer> orgnizationids = projectMapper.ListOrgnizationidsByProjectid(projectid);
        lqw.in(Orgnization::getId,orgnizationids);
        List<Orgnization> orgnizations =  orgnizationMapper.selectList(lqw);
        return new ArrayList<>(orgnizations);
    }



    /*
     * @title :AddOrganization
     * @Author :Lin
     * @Description : 添加组织 仅项目管理员
     * @Date :17:35 2023/7/14
     * @Param :[orgnization]
     * @return :void
     **/
    @PreAuthorize("hasRole('PA')")
    @Override
    public void AddOrganization(Orgnization orgnization){
        if (!ObjectUtils.isEmpty(orgnization))
        {
            //插入orgnization
            orgnizationMapper.insert(orgnization);
        }

    }

    /*
     * @title :AddOrganization_Project
     * @Author :Lin
     * @Description : 往项目中添加组织 仅项目管理员
     * @Date :17:39 2023/7/14
     * @Param :[orgnizationid, projectid]
     * @return :void
     **/
    @PreAuthorize("hasRole('PA')")
    @Override
    public void AddOrganization_Project(Integer orgnizationid,Integer projectid){
        orgnizationMapper.AddOrgnization_Project(orgnizationid,projectid);
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
    public void AddSmallRoutineUser(User user,Integer orgnizationid) {
        userMapper.insert(user);
        orgnizationMapper.AddOrgnization_User(user.getId(),orgnizationid);
//        projectMapper.AddProject_User();
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
        lqw.eq(User::getDelFlag,0);
        User user = userMapper.selectOne(lqw);
        if(!ObjectUtils.isEmpty(user)){
            return false;
        }
        return true ;
    }

    /*
     * @title :AddRole
     * @Author :Lin
     * @Description : 增加角色，并维护角色与项目、组织、权限的关系     仅限拥有角色管理权限的用户
     * @Date :14:10 2023/3/11
     * @Param :[role, projectid, authorities, quantity, orgnizationids]
     * @return :void
     **/
    @PreAuthorize("hasAuthority('role_management')")
    @Override
    public void AddRole(Role role, Integer projectid,
                        List<Integer> authorities,Integer quantity,
                        List<Integer> orgnizationids) {
        roleMapper.insert(role);
        //该项目下有什么角色
        projectMapper.AddProject_Roles(projectid,role.getId(),quantity);
        //该角色对某些组织有数据权限
        for (Integer orgnizationid : orgnizationids) {
            roleMapper.AddRole_Orgnization(role.getId(),orgnizationid);
        }
        //该角色有什么权限
        for (Integer authority : authorities) {
            authorityMapper.AddRole_Menu(role.getId(),authority);
        }
    }
    /*
     * @title :ListRolesByProjectId
     * @Author :Lin
     * @Description : 查询项目下有什么角色信息    仅限拥有角色管理权限的账户
     * @Date :11:00 2023/3/16
     * @Param :[projectid]
     * @return :void
     **/
    @PreAuthorize("hasAuthority('role_management')")
    @Override
    public ArrayList ListRolesByProjectId(Integer projectid){

        ArrayList<Map<String, Object>> res = new ArrayList<>();
        Collection<Project_Role> project_roles = projectMapper.ListProject_RoleByProjectid(projectid);
        LambdaQueryWrapper<Role> lqw = new LambdaQueryWrapper<Role>();
        for (Project_Role project_role : project_roles) {
            Map<String, Object> map = new HashMap<String,Object>();
            Role role = roleMapper.selectById(project_role.getRoleId());
            map.put("role",role);
            map.put("max",project_role.getMaxAccount());
            map.put("current",project_role.getCurrentAccount());
            res.add(map);
        }

        return res;
    }


}
