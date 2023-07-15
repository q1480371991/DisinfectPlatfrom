package com.example.disinfectplatfrom.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.disinfectplatfrom.Mapper.OrgnizationMapper;
import com.example.disinfectplatfrom.Pojo.Orgnization;
import com.example.disinfectplatfrom.Pojo.User;
import com.example.disinfectplatfrom.Service.OrgnizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.List;

/**
 * @author : Lin
 * @version : [v1.0]
 * @className : OrgnizationServiceImp
 * @description :
 * @createTime : 2023/3/1 22:53
 * @updateUser : Lin
 * @updateTime : 2023/3/1 22:53
 * @updateRemark : 描述说明本次修改内容
 */
@Service
public class OrgnizationServiceImp implements OrgnizationService {
    @Autowired
    private OrgnizationMapper orgnizationMapper;

    /*
     * @title :ListAllOrgnization
     * @Author :Lin
     * @Description : 查看所有组织  仅限项目管理员
     * @Date :14:10 2023/7/15
     * @Param :[]
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.Orgnization>
     **/
    @PreAuthorize("hasRole('PA')")
    @Override
    public Collection<Orgnization> ListAllOrgnization() {
        LambdaQueryWrapper<Orgnization> lqw = new LambdaQueryWrapper<Orgnization>();
        List<Orgnization> orgnizations = orgnizationMapper.selectList(lqw);
        return orgnizations;
    }


    /*
     * @title :SelectOrgnization
     * @Author :Lin
     * @Description : //模糊查询所有组织  仅限项目管理员
     * @Date :15:54 2023/7/15
     * @Param :[name, phonenum]
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.Orgnization>
     **/
    @PreAuthorize("hasRole('PA')")
    @Override
    public Collection<Orgnization> SelectOrgnization(String name, String phonenum){
        if (ObjectUtils.isEmpty(name)){
            name="";
        }
        if (ObjectUtils.isEmpty(phonenum)){
            phonenum="";
        }
        Collection<Orgnization> orgnizations = orgnizationMapper.SelectOrgnization(name, phonenum);
        return orgnizations;
    }
}
