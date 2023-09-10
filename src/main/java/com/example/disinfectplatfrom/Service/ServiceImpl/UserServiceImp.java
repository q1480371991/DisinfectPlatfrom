package com.example.disinfectplatfrom.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.disinfectplatfrom.Mapper.*;
import com.example.disinfectplatfrom.Pojo.*;
import com.example.disinfectplatfrom.Service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
     * @title :UpdatePassword
     * @Author :Lin
     * @Description : 修改账号密码
     * @Date :22:40 2023/3/1
     * @Param :[password]
     * @return :void
     **/
    @Override
    public void UpdatePhonenumber(String phonenumber) {
        //获取当前用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        //直接修改当前用户的密码，不需要前端传用户id
        user.setPhonenumber(phonenumber);
        userMapper.updateById(user);
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
    public void UpdateEmail(String email) {
        //获取当前用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        //直接修改当前用户的密码，不需要前端传用户id
        user.setEmail(email);
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
     * @title :ListUsers
     * @Author :Lin
     * @Description : 返回项目下所有账户内容    仅项目管理员 or HW
     * 海威顶级账号查看数据范围：所有项目的所有管理台账户内容。管理员查看数据范围：项目内所有管理台账户内容。
     * @Date :15:56 2023/7/16
     * @Param :[]
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.User>
     **/
    @PreAuthorize("hasRole('PA') or hasRole('HW')")
    @Override
    public Collection<User> ListUsers(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User current_user = (User)authentication.getPrincipal();
//        Collection<Map<String, Object>> maps = new HashSet<>();
        if (current_user.isHW()){
            ArrayList<Integer> userids = new ArrayList<>();
            Collection<Project> projects = this.ListAllProject();
            for (Project project : projects) {
                Collection<Integer> userids1 = userMapper.ListUserIdInProjectById(project.getId());
                userids.addAll(userids1);
            }
            LambdaQueryWrapper<User> userlqw = new LambdaQueryWrapper<User>();
            userlqw.in(User::getId,userids);
            List<User> users = userMapper.selectList(userlqw);
            return users;
        }else{
            return ListUserByProjectAdminId();
        }
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
     * @title :GetCurrentProject
     * @Author :Lin
     * @Description : 获取当前项目管理员所管理的项目信息  仅限项目管理员   未完成
     * @Date :14:50 2023/8/26
     * @Param :[]
     * @return :com.example.disinfectplatfrom.Pojo.Project
     **/
    @Override
    public Project GetCurrentProject(){
        return null;
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
            System.out.println(user);
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
        user.setRemark("项目管理员");
        userMapper.insert(user);
        //给user分配项目管理员的权限
        userMapper.AddUser_Role(user.getId(),3);
    }

    /*
     * @title :SelectUser
     * @Author :Lin
     * @Description : 模糊查询用户   仅限拥有高级设置权限  待改
     * @Date :16:55 2023/3/7
     * @Param :[s, projectid]
     * @return :void
     **/
    @PreAuthorize("hasAuthority('advanced_setting')")
    @Override
    public Collection<User> SelectUser(String username, Integer projectid,Integer status,String email,String phonenumber) {
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
        if(!ObjectUtils.isEmpty(username)) {
            lqw.like(User::getUsername, username);
        }

        List<User> users = userMapper.selectList(lqw);
        return users;
    }

    /*
     * @title :ListRolesByUserid
     * @Author :Lin
     * @Description : 通过userid获取 用户有什么角色
     * @Date :23:04 2023/9/8
     * @Param :[userid]
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.Role>
     **/
    @Override
    @PreAuthorize("hasRole('PA')")
    public Collection<Role> ListRolesByUserid(Integer userid){
        Collection<Integer> roleids = userMapper.ListUserRolesid(userid);
        LambdaQueryWrapper<Role> lqw = new LambdaQueryWrapper<Role>();
        lqw.in(Role::getId,roleids);
        List<Role> roles = roleMapper.selectList(lqw);
        return roles ;
    }

    /*
     * @title :AddProjectUser
     * @Author :Lin
     * @Description : 添加项目用户，仅限项目管理员PA
     * @Date :23:05 2023/3/6
     * @Param :[user]
     * @return :void
     **/
    @PreAuthorize("hasRole('PA')")
    @Override
    public void AddProjectUser(User user,ArrayList<Integer> roleids){
        //获取当前PA所管理的项目id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentuser = (User)authentication.getPrincipal();
        LambdaQueryWrapper<Project> lqw = new LambdaQueryWrapper<Project>();
        lqw.eq(Project::getAdminId,currentuser.getId());
        Project project = projectMapper.selectOne(lqw);
        Integer projectid = project.getProjectId();
        //先查询该项目下该角色的信息
        for (Integer roleid : roleids) {
            Project_Role project_role = projectMapper.SelectProject_Role(projectid, roleid);
            //判断
            if (project_role.getCurrentAccount()<project_role.getMaxAccount()){
                //用户的角色
                Role role = roleMapper.selectById(roleid);
                //当前角色的数量小于最大数量，添加用户
                user.setRemark(role.getRemark());
                user.setPassword("{noop}"+user.getPassword());
                userMapper.insert(user);
                //将角色与用户关联
                userMapper.AddUser_Role(user.getId(),roleid);
                //将用户与项目关联
                projectMapper.AddProject_User(projectid,user.getId());
                //更新该项目中该角色对应的current_account
                project_role.setCurrentAccount(project_role.getCurrentAccount()+1);
                projectMapper.UpdateProjectRoleAccount(project_role.getCurrentAccount(),project_role.getProjectId(),project_role.getRoleId());
            }else {
                //抛出异常
                throw new RuntimeException("当前角色role"+roleid+"数量已满");
            }
        }

    }


    /*
     * @title :UpdateUser
     * @Author :Lin
     * @Description : 编辑项目用户，仅限项目管理员PA   未构思好
     * @Date :23:10 2023/9/10
     * @Param :[user, roleids]
     * @return :void
     **/
    @Override
    @PreAuthorize("hasRole('PA')")
    public void UpdateUser(User user, ArrayList<Integer> roleids){
        User olduser = userMapper.selectById(user.getId());
//        olduser.setUsername();

    }

    /*
     * @title :AddOrgnizationAdmin
     * @Author :Lin
     * @Description :  添加组织管理员 与添加组织一并进行  仅限项目管理员
     * @Date :15:13 2023/8/29
     * @Param :[user, orgnizationid]
     * @return :void
     **/
    @PreAuthorize("hasRole('PA')")
    @Override
    public void AddOrgnizationAdmin(User user, Integer orgnizationid){
        if (!ObjectUtils.isEmpty(user) && !ObjectUtils.isEmpty(orgnizationid)){
            //添加用户
            user.setRemark("组织管理员");
            userMapper.insert(user);
            //将用户与项目关联
            orgnizationMapper.AddOrgnization_User(user.getId(), orgnizationid);
            //绑定角色 2:组织管理员
            userMapper.AddUser_Role(user.getId(),2);
        }else{
            throw new RuntimeException("添加组织管理员失败");
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
            int insert = orgnizationMapper.insert(orgnization);
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
        user.setRemark("小程序用户");
        userMapper.insert(user);
        orgnizationMapper.AddOrgnization_User(user.getId(),orgnizationid);
//        projectMapper.AddProject_User();
    }

    /*
     * @title :CheckProjectName
     * @Author :Lin
     * @Description : 检测用户账户名是否重复    仅项目管理员
     * @Date :21:34 2023/3/7
     * @Param :[projectname]
     * @return :boolean
     **/
    @PreAuthorize("hasRole('ROLE_PA')")
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
     * @Description : 增加角色，并维护角色与项目、组织、权限的关系     仅限PA
     * @Date :14:10 2023/3/11
     * @Param :[role,authorities, quantity]
     * @return :void
     **/
    @PreAuthorize("hasRole('PA')")
    @Override
    public void AddRole(Role role, List<Integer> authorities,Integer quantity) {
        if (!ObjectUtils.isEmpty(role)||!ObjectUtils.isEmpty(authorities)||!ObjectUtils.isEmpty(quantity)){
            //通过当前用户查询项目id
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User)authentication.getPrincipal();
            LambdaQueryWrapper<Project> lqw = new LambdaQueryWrapper<Project>();
            lqw.eq(Project::getAdminId,user.getId());
            Project project = projectMapper.selectOne(lqw);
            Integer projectid = project.getProjectId();

            //插入角色
            roleMapper.insert(role);
            //该项目下有什么角色
            projectMapper.AddProject_Roles(projectid,role.getId(),quantity);
            //该角色对某些组织有数据权限  暂时不考虑
//        for (Integer orgnizationid : orgnizationids) {
//            roleMapper.AddRole_Orgnization(role.getId(),orgnizationid);
//        }
            //该角色有什么权限
            for (Integer authority : authorities) {
                authorityMapper.AddRole_Menu(role.getId(),authority);
            }
        }

    }
    /*
     * @title :ListRolesByProjectId
     * @Author :Lin
     * @Description : 通过项目id查询项目下有什么角色信息     仅限HW or PA
     * @Date :11:00 2023/3/16
     * @Param :[projectid]
     * @return :void
     **/

    @Override
    @PreAuthorize("hasRole('PA') or hasRole('HW')")
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

    /*
     * @title :ListRoles
     * @Author :Lin
     * @Description : 查询项目下有什么角色信息  仅限拥有HW or 项目管理员
     * HW：所有项目下的角色信息   项目管理员：所管理项目下的角色信息
     * @Date :20:27 2023/9/1
     * @Param :[]
     * @return :java.util.ArrayList
     **/
    @PreAuthorize("hasRole('HW') or hasRole('PA')")
    @Override
    public ArrayList ListRoles(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        ArrayList res = new ArrayList<>();
        if (user.isHW()){
            //HW
            //查询所有项目下的角色信息
            Collection<Project> projects = ListAllProject();
            for (Project project : projects) {
                ArrayList arrayList = ListRolesByProjectId(project.getProjectId());
                res.addAll(arrayList);
            }
        }else{
            //PA
            //查询PA所管理的项目下的角色信息
            //先根据userid查出该用户是哪个项目的管理员
            LambdaQueryWrapper<Project> lqw = new LambdaQueryWrapper<Project>();
            lqw.eq(Project::getAdminId,user.getId());
            Project project = projectMapper.selectOne(lqw);
            res = ListRolesByProjectId(project.getProjectId());
        }



        return res ;
    }





    /*
     * @title :ListAllMenus
     * @Author :Lin
     * @Description : 查询所有权限 仅限HW or PA
     * @Date :16:09 2023/9/3
     * @Param :[]
     * @return :java.util.List<com.example.disinfectplatfrom.Pojo.Authority>
     **/
    @Override
    @PreAuthorize("hasRole('HW') or hasRole('PA')")
    public List<Authority> ListAllMenus(){
        LambdaQueryWrapper<Authority> lqw = new LambdaQueryWrapper<Authority>();
        List<Authority> authorities = authorityMapper.selectList(lqw);
        return authorities;
    }


    /*
     * @title :ListMenusByRoleid
     * @Author :Lin
     * @Description : 通过roleid查询角色权限 仅限HW or PA
     * @Date :10:13 2023/9/2
     * @Param :[roleid]
     * @return :java.util.List<com.example.disinfectplatfrom.Pojo.Authority>
     **/
    @PreAuthorize("hasRole('HW') or hasRole('PA')")
    @Override
    public List<Authority> ListMenusByRoleid(Integer roleid){
        Collection<Integer> menuids = roleMapper.ListMenusByRoleid(roleid);
        LambdaQueryWrapper<Authority> lqw = new LambdaQueryWrapper<Authority>();
        lqw.in(Authority::getId,menuids);
        List<Authority> authorities = authorityMapper.selectList(lqw);
        return authorities;
    }


    /*
     * @title :UpdateRole
     * @Author :Lin
     * @Description : 修改角色，并维护角色与项目、组织、权限的关系     仅限PA
     * @Date :21:39 2023/9/4
     * @Param :[role, authorities, quantity]
     * @return :void
     **/
    @Override
    @PreAuthorize("hasRole('PA')")
    public void UpdateRole(Role role, List<Integer> authorities,Integer quantity){
        if (!ObjectUtils.isEmpty(role)||!ObjectUtils.isEmpty(authorities)||!ObjectUtils.isEmpty(quantity)){
            //通过当前用户查询项目id
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User)authentication.getPrincipal();
            LambdaQueryWrapper<Project> lqw = new LambdaQueryWrapper<Project>();
            lqw.eq(Project::getAdminId,user.getId());
            Project project = projectMapper.selectOne(lqw);
            Integer projectid = project.getProjectId();

            //更新最大人数
            Project_Role project_role = projectMapper.SelectProject_Role(projectid, role.getId());
            if (quantity<project_role.getCurrentAccount()){
                throw new RuntimeException("角色最大人数小于当前角色人数");
            }
            projectMapper.UpdateProjectRoleMax(projectid,role.getId(),quantity);

            //修改角色
            roleMapper.updateById(role);

            //先删除角色原来的权限
            LambdaQueryWrapper<Authority> aulqw = new LambdaQueryWrapper<Authority>();
            Collection<Integer> menuids = authorityMapper.ListAuthoritiesidsByRoleId(role.getId());
            for (Integer menuid : menuids) {
                roleMapper.DelectRole_Menu(role.getId(),menuid);
            }

            //然后加入新权限
            for (Integer authorityid : authorities) {
                authorityMapper.AddRole_Menu(role.getId(),authorityid);
            }


        }
    }

}
