package com.example.disinfectplatfrom.Controller;
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

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
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
        boolean b = projectService.CheckProjectId(projectid);
        return R.ok(b);
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
        return R.ok(projectService.CheckProjectName(projectname));
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
     * @title :ListRolesBy
     * @Author :Lin
     * @Description : 查询项目下有什么角色信息    仅限拥有角色管理权限的账户
     * @Date :17:40 2023/7/15
     * @Param :[projectid]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/ListRolesBy",method = RequestMethod.GET)
    public R ListRolesBy(@RequestParam Integer projectid){
        if (!ObjectUtils.isEmpty(projectid)){
            ArrayList data = userService.ListRolesByProjectId(projectid);
            return R.ok(data);
        }
        return R.fail(null);
    }

    /*
     * @title :AddRole
     * @Author :Lin
     * @Description :  添加一个项目下的角色   仅限拥有角色管理权限的账户    已解决前后端参数传递的问题
     * @Date :17:52 2023/7/15
     * @Param :[role, projectid, authorities, quantity, orgnizationids]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/AddRole",method = RequestMethod.POST)
    public R AddRole(@RequestBody String data) throws JsonProcessingException {
        if (!ObjectUtils.isEmpty(data)){
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(data);
            Role role = mapper.convertValue(jsonNode.get("role"), Role.class);
            int projectid = jsonNode.get("projectid").asInt();
            ArrayList<Integer> authorities = mapper.convertValue(jsonNode.get("authorities"), ArrayList.class);
            int quantity = jsonNode.get("quantity").asInt();
            ArrayList<Integer> orgnizationids = mapper.convertValue(jsonNode.get("orgnizationids"), ArrayList.class);
            userService.AddRole(role,projectid,authorities,quantity,orgnizationids);
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
            Collection<User> users = userService.SelectUser((String) data.get("username"), (Integer) data.get("projectid"),
                    (Integer) data.get("status"), (String) data.get("email"), (String) data.get("phonenumber"));
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
     * @title :ListRoles
     * @Author :Lin
     * @Description : 查询项目下有什么角色信息    仅限拥有角色管理权限的账户
     * @Date :16:45 2023/7/16
     * @Param :[projectid]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/ListRoles",method = RequestMethod.GET)
    public R ListRoles(@RequestParam("projectid") Integer projectid){
        if (!ObjectUtils.isEmpty(projectid)){
            return R.ok(userService.ListRolesByProjectId(projectid));
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
