package com.example.disinfectplatfrom.Controller;

import com.example.disinfectplatfrom.Pojo.Project;
import com.example.disinfectplatfrom.Pojo.Role;
import com.example.disinfectplatfrom.Service.ProjectService;
import com.example.disinfectplatfrom.Service.UserService;
import com.example.disinfectplatfrom.Utils.R;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping("/ListAllProject")
    public R ListAllProject(){
        Collection<Project> projects = userService.ListAllProject();
        return R.ok(projects);
    }

    /*
     * @title :AddProject
     * @Author :Lin
     * @Description : 添加项目   仅限海威账号
     * @Date :16:41 2023/7/15
     * @Param :[project]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/AddProject",method = RequestMethod.POST)
    public R AddProject(@RequestBody Project project){
        if(!ObjectUtils.isEmpty(project)){
            userService.AddProject(project);
        }
        return R.ok(null);
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
    public R DeleteProject(@RequestBody Map<String, Object> data){
        if (!ObjectUtils.isEmpty(data)){
            projectService.DeleteProjectById((Integer) data.get("projectid"), (String) data.get("password"));
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
     * @Description :  添加一个项目下的角色   仅限拥有角色管理权限的账户    未解决前后端参数传递的问题
     * @Date :17:52 2023/7/15
     * @Param :[role, projectid, authorities, quantity, orgnizationids]
     * @return :com.example.disinfectplatfrom.Utils.R
     **/
    @RequestMapping(value = "/AddRole",method = RequestMethod.POST)
    public R AddRole(Role role, Integer projectid,
                     List<Integer> authorities,Integer quantity,
                     List<Integer> orgnizationids){
        if (!ObjectUtils.isEmpty(role) &&!ObjectUtils.isEmpty(projectid)
                &&!ObjectUtils.isEmpty(authorities)&&!ObjectUtils.isEmpty(quantity)
                &&!ObjectUtils.isEmpty(orgnizationids)){
            userService.AddRole(role,projectid,authorities,quantity,orgnizationids);
        }
        return R.ok(null);
    }
    //账号管理





}
