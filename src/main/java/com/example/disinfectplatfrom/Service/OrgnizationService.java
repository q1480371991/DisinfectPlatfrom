package com.example.disinfectplatfrom.Service;

import com.example.disinfectplatfrom.Pojo.Orgnization;

import java.util.Collection;

public interface OrgnizationService {
    //查看所有组织  仅限项目管理员
    public Collection<Orgnization> ListAllOrgnization();

    //模糊查询所有组织  仅限项目管理员
    public Collection<Orgnization> SelectOrgnization(String name,String phonenum);
}
