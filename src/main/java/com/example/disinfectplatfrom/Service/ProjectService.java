package com.example.disinfectplatfrom.Service;

import com.example.disinfectplatfrom.Pojo.Orgnization;
import org.springframework.stereotype.Service;

import java.util.Collection;


public interface ProjectService {
    public boolean CheckProjectId(Integer projectid);
    public boolean CheckProjectName(String projectname);
    public void DeleteProjectById(Integer projectid,String password);
    public Collection<Orgnization> ListOrgnizationByProjectid(Integer projectid);
}
