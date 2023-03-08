package com.example.disinfectplatfrom.Service;

import org.springframework.stereotype.Service;


public interface ProjectService {
    public boolean CheckProjectId(Integer projectid);
    public boolean CheckProjectName(String projectname);
    public void DeleteProjectById(Integer projectid);
}
