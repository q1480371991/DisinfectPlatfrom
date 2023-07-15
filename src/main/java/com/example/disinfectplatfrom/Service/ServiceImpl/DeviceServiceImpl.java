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
import org.springframework.security.access.prepost.PreAuthorize;
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

    /*
     * @title :ListDeviceByOrignizationId
     * @Author :Lin
     * @Description : 获得组织下所有设备   仅限项目管理员
     * @Date :14:00 2023/7/15
     * @Param :[deviceid]
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.Device>
     **/
    @PreAuthorize("hasRole('PA')")
    @Override
    public Collection<Device> ListDeviceByOrignizationId(Integer deviceid){
        Collection<Device> devices = deviceMapper.ListDeviceByOrignizationId(deviceid);
        if (ObjectUtils.isEmpty(devices)) {
            return null;
        }
        return devices;
    }

    /*
     * @title :ListDeviceByProjectId
     * @Author :Lin
     * @Description :  获得项目下所有设备   仅限？？？
     * @Date :13:58 2023/7/15
     * @Param :[projectid]
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.Device>
     **/
//    @PreAuthorize("hasAuthority('mocha_itom')")
    @Override
    public Collection<Device> ListDeviceByProjectId(Integer projectid){
        Collection<Device>res = new ArrayList<Device>();
        Collection<Integer> orgnizationids = projectMapper.ListOrgnizationidsByProjectid(projectid);
        for (Integer orgnizationid  : orgnizationids) {
            res.addAll(this.ListDeviceByOrignizationId(orgnizationid));
        }
        return res;
    }
    /*
     * @title :AddDevice
     * @Author :Lin
     * @Description : 添加设备   仅组织管理员
     * @Date :21:36 2023/7/14
     * @Param :[device]
     * @return :void
     **/
    @PreAuthorize("hasRole('OA') and hasAuthority('mocha_itom')")
    @Override
    public void AddDevice(Device device,Integer orgnizationid)
    {

        if (!ObjectUtils.isEmpty(device)){
            deviceMapper.insert(device);
            deviceMapper.AddOrgnization_Device(orgnizationid,device.getId());
        }

    }
}
