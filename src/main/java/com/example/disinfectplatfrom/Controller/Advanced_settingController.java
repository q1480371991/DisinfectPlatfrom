package com.example.disinfectplatfrom.Controller;
import com.example.disinfectplatfrom.Pojo.Authority;
import com.example.disinfectplatfrom.Pojo.Project;
import com.example.disinfectplatfrom.Pojo.Role;
import com.example.disinfectplatfrom.Pojo.User;
import com.example.disinfectplatfrom.Service.ProjectService;
import com.example.disinfectplatfrom.Service.UserService;
import com.example.disinfectplatfrom.Utils.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author : Lin
 * @version : [v1.0]
 * @className : Advanced_settingController
 * @description : 高级设置controller
 * @createTime : 2023/6/25 20:31
 * @updateUser : Lin
 * @updateTime : 2023/6/25 20:31
 * @updateRemark : 描述说明本次修改内容
 */
@RestController
@PreAuthorize("hasAuthority('advanced_setting')")
@RequestMapping("/advanced_setting")
public class Advanced_settingController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;


    //项目管理 仅限海威账号
    /*
     * @title :ListAllProject
     * @Author :Lin
     * @Description : 返回所有项目的信息  仅限海威账号
     * @Date :16:31 2023/7/15
     * @Param :[]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/ListAllProject",method = RequestMethod.GET)
    public R ListAllProject(){
        Collection<Project> projects = userService.ListAllProject();
        return R.ok(projects);
    }

    /*
     * @title :AddProject
     * @Author :Lin
     * @Description : 添加项目   仅限海威账号
     * 在该接口中同时处理 添加项目和添加项目初始账户
     * @Date :16:41 2023/7/15
     * @Param :[project]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/AddProject",method = RequestMethod.POST)
    public R AddProject(@RequestBody String data) throws JsonProcessingException {
        if (!ObjectUtils.isEmpty(data)){
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(data);
            System.out.println(jsonNode);
            User user = mapper.convertValue(jsonNode.get("user"), User.class);
            user.setPassword("{noop}"+user.getPassword());
            Project project = mapper.convertValue(jsonNode.get("project"), Project.class);
            project.setCreatTime(new Timestamp(System.currentTimeMillis()));
            System.out.println(project);
            System.out.println(user);


            userService.AddProject(project);
            userService.AddProjectOriginAccount(project.getProjectId(),user);

            return R.ok(null);
        }

        return R.fail(null);
    }

    /*
     * @title :UpdateProject
     * @Author :Lin
     * @Description : 更新项目   仅限海威账号
     * @Date :16:41 2023/7/15
     * @Param :[projectid, projectname, remark]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/UpdateProject",method = RequestMethod.POST)
    public R UpdateProject(@RequestBody Map<String, Object> data){
        if (!ObjectUtils.isEmpty(data)){
            userService.UpdateProjectById((Integer) data.get("projectid"), (String) data.get("projectname"),(String)data.get("remark"));
        }
        return R.ok(null);
    }


    /*
     * @title :DeleteProject
     * @Author :Lin
     * @Description : 删除项目    仅限海威账号
     * @Date :16:42 2023/7/15
     * @Param :[projectid, password]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/DeleteProject",method = RequestMethod.POST)
    public R DeleteProject(@RequestBody Map<String, Object> data) throws Exception {
        if (!ObjectUtils.isEmpty(data)){
            try{
                projectService.DeleteProjectById((Integer) data.get("projectid"), (String) data.get("password"));
            }catch (Exception e){
                System.out.println(e);
                return R.fail("无法删除");
            }
        }
        return R.ok(null);
    }

    /*
     * @title :CheckProjectId
     * @Author :Lin
     * @Description : 检查项目id
     * @Date :16:44 2023/7/15
     * @Param :[projectid]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/CheckProjectId",method = RequestMethod.GET)
    public R CheckProjectId(@RequestParam("projectid") Integer projectid){
        System.out.println("CheckProjectId");
        if(!ObjectUtils.isEmpty(projectid)){
            boolean b = projectService.CheckProjectId(projectid);
            return R.ok(b);
        }
        return R.fail(null);
    }

    /*
     * @title :CheckProjectName
     * @Author :Lin
     * @Description : 检查项目名字
     * @Date :16:46 2023/7/15
     * @Param :[projectname]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/CheckProjectName",method = RequestMethod.GET)
    public R CheckProjectName (@RequestParam("projectname") String projectname){

        if(!ObjectUtils.isEmpty(projectname)){
            return R.ok(projectService.CheckProjectName(projectname));
        }
        return R.fail(null);
    }

    /*
     * @title :AddProjectOriginAccount
     * @Author :Lin
     * @Description : 添加项目初始账户  仅限项目管理员
     * @Date :19:46 2023/8/25
     * @Param :[data]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/AddProjectOriginAccount",method = RequestMethod.POST)
    public R AddProjectOriginAccount (@RequestBody String data) throws JsonProcessingException {
        if (!ObjectUtils.isEmpty(data)){
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(data);
            User user = mapper.convertValue(jsonNode.get("user"), User.class);
            int projectid = jsonNode.get("projectid").asInt();
            System.out.println(user);
            userService.AddProjectOriginAccount(projectid,user);
            return R.ok(null);
        }
        return R.fail(null);


    }

    //角色管理


    /*
     * @title :ListRoles
     * @Author :Lin
     * @Description : 查询项目下有什么角色信息    仅限拥有角色管理权限的账户
     * HW：所有项目下的角色信息   项目管理员：所管理项目下的角色信息
     * @Date :17:40 2023/7/15
     * @Param :[projectid]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @PreAuthorize("hasAuthority('role_management')")
    @RequestMapping(value = "/ListRoles",method = RequestMethod.GET)
    public R ListRoles(){
        ArrayList data = userService.ListRoles();
        if (!ObjectUtils.isEmpty(data)){
            return R.ok(data);
        }

        return R.fail(null,"项目下没有角色");
    }


    /*
     * @title :ListRolesByProjectid
     * @Author :Lin
     * @Description : 通过项目id查询项目下有什么角色信息    仅限HW
     * @Date :21:08 2023/9/1
     * @Param :[projectid]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @PreAuthorize("hasAuthority('role_management') and hasRole('HW')")
    @RequestMapping(value = "/ListRolesByProjectid",method = RequestMethod.GET)
    public R ListRolesByProjectid(@PathParam("projectid") Integer projectid){
        if (!ObjectUtils.isEmpty(projectid)){
            ArrayList arrayList = userService.ListRolesByProjectId(projectid);
            return R.ok(arrayList);
        }
        return R.fail(null);
    }


    /*
     * @title :ListAllMenus
     * @Author :Lin
     * @Description : 查询所有权限 仅限拥有角色管理权限的账户
     * @Date :16:10 2023/9/3
     * @Param :[]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/ListAllMenus",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('role_management')")
    public R ListAllMenus(){
        List<Authority> authorities = userService.ListAllMenus();
        return R.ok(authorities);

    }

    /*
     * @title :ListMenusByRoleid
     * @Author :Lin
     * @Description : 通过roleid查询角色权限  仅限HW or PA
     * @Date :10:07 2023/9/2
     * @Param :[roleid]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/ListMenusByRoleid",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('role_management')")
    public R ListMenusByRoleid(@PathParam("roleid") Integer roleid){
        if (!ObjectUtils.isEmpty(roleid)){
            List<Authority> authorities = userService.ListMenusByRoleid(roleid);
            return R.ok(authorities);
        }


        return R.fail(null);
    }



    /*
     * @title :AddRole
     * @Author :Lin
     * @Description :  添加一个项目下的角色   仅限拥有角色管理权限的账户
     * @Date :17:52 2023/7/15
     * @Param :[role, projectid, authorities, quantity, orgnizationids]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @PreAuthorize("hasAuthority('role_management')")
    @RequestMapping(value = "/AddRole",method = RequestMethod.POST)
    public R AddRole(@RequestBody String data) throws JsonProcessingException {
        if (!ObjectUtils.isEmpty(data)){
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(data);
            Role role = mapper.convertValue(jsonNode.get("role"), Role.class);

            ArrayList<Integer> authorities = mapper.convertValue(jsonNode.get("authorities"), ArrayList.class);
            int quantity = jsonNode.get("quantity").asInt();


            //暂不考虑数据权限：得重构数据库表
//            ArrayList<Integer> orgnizationids = mapper.convertValue(jsonNode.get("orgnizationids"), ArrayList.class);
            userService.AddRole(role,authorities,quantity);
            return R.ok(null);
        }

        return R.fail(null);
    }


    /*
     * @title :UpdateRole
     * @Author :Lin
     * @Description : 修改角色信息   仅限拥有角色管理权限的账户
     * @Date :22:08 2023/9/4
     * @Param :[data]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @PreAuthorize("hasAuthority('role_management')")
    @RequestMapping(value = "/UpdateRole",method = RequestMethod.POST)
    public R UpdateRole(@RequestBody String data) throws JsonProcessingException {
        if (!ObjectUtils.isEmpty(data)){
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(data);
            Role role = mapper.convertValue(jsonNode.get("role"), Role.class);

            ArrayList<Integer> authorities = mapper.convertValue(jsonNode.get("authorities"), ArrayList.class);
            int quantity = jsonNode.get("quantity").asInt();

            userService.UpdateRole(role,authorities,quantity);
            return R.ok(null);
        }


        return R.fail(null);
    }






    //账号管理

    /*
     * @title :SelectUser
     * @Author :Lin
     * @Description : 模糊查询用户 仅限拥有高级设置权限
     *
     * @Date :20:24 2023/7/15
     * @Param :[data]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/SelectUser",method = RequestMethod.POST)
    public R SelectUser(@RequestBody Map<String, Object> data){
        if (!ObjectUtils.isEmpty(data)){
            System.out.println(data);
            String username =(String) data.get("username");
            Integer projectid = (Integer) data.get("projectid");
            Integer status =(Integer) data.get("status")  ;
            String email = (String) data.get("email");
            String phonenumber = (String) data.get("phonenumber");
            Collection<User> users = userService.SelectUser(username,projectid ,
                  status  , email,phonenumber );
            return R.ok(users);
        }
        return R.fail(null);
    }

    /*
     * @title :ListUsers
     * @Author :Lin
     * @Description : 查询项目下的账户信息
     * 海威顶级账号查看数据范围：所有项目的所有管理台账户内容。管理员查看数据范围：项目内所有管理台账户内容。
     * @Date :20:27 2023/7/15
     * @Param :[]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/ListUsers",method = RequestMethod.POST)
    public R ListUsers(){
        Collection<User> users = userService.ListUsers();
        if (!ObjectUtils.isEmpty(users)){
            return R.ok(users);
        }
        return R.fail(null);
    }



    /*
     * @title :CheckUserName
     * @Author :Lin
     * @Description : 检测用户账户名是否重复    仅项目管理员
     * @Date :16:50 2023/7/16
     * @Param :[username]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value="/CheckUserName",method = RequestMethod.GET)
    public R CheckUserName(@RequestParam("username")String username){
        if (!ObjectUtils.isEmpty(username)){
            return R.ok(userService.CheckUserName(username));
        }
        return R.fail(null);
    }


    /*
     * @title :AddProjectUser
     * @Author :Lin
     * @Description : 添加项目用户，仅限项目管理员   已解决前后端传参的问题
     * @Date :16:54 2023/7/16
     * @Param :[data]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/AddProjectUser",method = RequestMethod.POST)
    public R AddProjectUser(@RequestBody String data) throws JsonProcessingException {
        if (!ObjectUtils.isEmpty(data)){
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(data);
            User user = mapper.convertValue(jsonNode.get("user"), User.class);
            int projectid = jsonNode.get("roleid").asInt();
            ArrayList<Integer> roleids = mapper.convertValue(jsonNode.get("roleid"), ArrayList.class);
            userService.AddProjectUser(user,projectid,roleids);
        }
        return R.fail(null);
    }

    //系统设置  账号设置





}
