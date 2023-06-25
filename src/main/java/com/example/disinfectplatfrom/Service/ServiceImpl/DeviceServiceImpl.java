package com.example.disinfectplatfrom.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.disinfectplatfrom.Mapper.DeviceMapper;
import com.example.disinfectplatfrom.Mapper.ProjectMapper;
import com.example.disinfectplatfrom.Pojo.Device;
import com.example.disinfectplatfrom.Pojo.Orgnization;
import com.example.disinfectplatfrom.Pojo.User;
import com.example.disinfectplatfrom.Service.DeviceService;
import com.example.disinfectplatfrom.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author : Lin
 * @version : [v1.0]
 * @className : DeviceServiceImpl
 * @description : 描述说明该类的功能
 * @createTime : 2023/6/20 15:56
 * @updateUser : Lin
 * @updateTime : 2023/6/20 15:56
 * @updateRemark : 描述说明本次修改内容
 */

@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    DeviceMapper deviceMapper;
    @Override
    public Collection<Device> ListDeviceByOrignizationId(Integer deviceid){
        Collection<Device> devices = deviceMapper.ListDeviceByOrignizationId(deviceid);
        if (ObjectUtils.isEmpty(devices)) {
            return null;
        }
        return devices;
    }

    @Override
    public Collection<Device> ListDeviceByProjectId(Integer projectid){
        Collection<Device>res = new ArrayList<Device>();
        Collection<Integer> orgnizationids = projectMapper.ListOrgnizationidsByProjectid(projectid);
        for (Integer orgnizationid  : orgnizationids) {
            res.addAll(this.ListDeviceByOrignizationId(orgnizationid));
        }
        return res;
    }
    @Override
    public void AddDevice(Device device,Integer orgnizationid)
    {
//        deviceMapper.insert(device)
        deviceMapper.insert(device);
        deviceMapper.AddOrgnization_Device(orgnizationid,device.getId());

    }
}
