package com.example.disinfectplatfrom.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.disinfectplatfrom.Mapper.ProjectMapper;
import com.example.disinfectplatfrom.Pojo.Project;
import com.example.disinfectplatfrom.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class ProjectServiceImp implements ProjectService {
    @Autowired
    private ProjectMapper projectMapper;
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
        lqw.eq(Project::getProjectid,projectid);
        Project project = projectMapper.selectOne(lqw);
        if(!ObjectUtils.isEmpty(project))return true;
        else return false;
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
        lqw.eq(Project::getProjectname,projectname);
        Project project = projectMapper.selectOne(lqw);
        if(!ObjectUtils.isEmpty(project))return true;
        else return false;
    }
}
