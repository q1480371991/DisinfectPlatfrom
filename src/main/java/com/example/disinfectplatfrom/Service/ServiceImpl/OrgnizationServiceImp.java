package com.example.disinfectplatfrom.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.disinfectplatfrom.Mapper.OrgnizationMapper;
import com.example.disinfectplatfrom.Pojo.Orgnization;
import com.example.disinfectplatfrom.Pojo.User;
import com.example.disinfectplatfrom.Service.OrgnizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class OrgnizationServiceImp implements OrgnizationService {
    @Autowired
    private OrgnizationMapper orgnizationMapper;
    @Override
    public Collection<Orgnization> ListAllOrgnization() {
        LambdaQueryWrapper<Orgnization> lqw = new LambdaQueryWrapper<Orgnization>();
        List<Orgnization> orgnizations = orgnizationMapper.selectList(lqw);
        return orgnizations;
    }
}
