package com.example.disinfectplatfrom.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.disinfectplatfrom.Mapper.OrgnizationMapper;
import com.example.disinfectplatfrom.Mapper.ProjectMapper;
import com.example.disinfectplatfrom.Pojo.Orgnization;
import com.example.disinfectplatfrom.Pojo.Project;
import com.example.disinfectplatfrom.Pojo.User;
import com.example.disinfectplatfrom.Service.ProjectService;
import com.example.disinfectplatfrom.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.List;

@Service
public class ProjectServiceImp implements ProjectService {
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private OrgnizationMapper orgnizationMapperg;

    @Autowired
    private UserService userService;

    /*
     * @title :DeleteProjectById
     * @Author :Lin
     * @Description : 逻辑删除项目：把项目的del_flag改为1  逻辑删除没有弄清楚 ,待改
     * @Date :23:04 2023/3/8
     * @Param :[projectid]
     * @return :void
     **/
    @Override
    public void DeleteProjectById(Integer projectid,String password) {
        Collection<User> users = userService.ListUserByProjectId(projectid,1);
        //当一个项目没有除项目管理员和项目创始人账号时才能删除
        if (ObjectUtils.isEmpty(users)&&password.equalsIgnoreCase(password)){
            Project project = projectMapper.selectById(projectid);
            //逻辑删除
            project.setDelFlag(1);
            projectMapper.updateById(project);
        }
    }

    /*
     * @title :CheckProjectId
     * @Author :Lin
     * @Description : 检查项目id是否重复,true重复，false不重复
     * @Date :21:25 2023/3/7
     * @Param :[projectid]
     * @return :boolean
     **/
    @Override
    public boolean CheckProjectId(Integer projectid) {
        LambdaQueryWrapper<Project> lqw = new LambdaQueryWrapper<Project>();
        lqw.eq(Project::getDelFlag,0);//查没有被逻辑删除的项目
        lqw.eq(Project::getProjectId,projectid);
        Project project = projectMapper.selectOne(lqw);
        if(!ObjectUtils.isEmpty(project)) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public Collection<Orgnization> ListOrgnizationByProjectid(Integer projectid) {
        Collection<Integer> projectids = projectMapper.ListOrgnizationidsByProjectid(projectid);
        LambdaQueryWrapper<Orgnization> lqw = new LambdaQueryWrapper<Orgnization>();
        lqw.in(Orgnization::getId,projectids);
        List<Orgnization> orgnizations = orgnizationMapperg.selectList(lqw);
        return orgnizations;
    }

    /*
     * @title :CheckProjectId
     * @Author :Lin
     * @Description : 检查项目名称是否重复
     * @Date :21:25 2023/3/7
     * @Param :[projectid]
     * @return :boolean
     **/
    @Override
    public boolean CheckProjectName(String projectname) {
        LambdaQueryWrapper<Project> lqw = new LambdaQueryWrapper<Project>();
        lqw.eq(Project::getDelFlag,0);//查没有被逻辑删除的项目
        lqw.eq(Project::getProjectName,projectname);
        Project project = projectMapper.selectOne(lqw);
        if(!ObjectUtils.isEmpty(project)) {
            return true;
        }
        else {
            return false;
        }
    }
}
