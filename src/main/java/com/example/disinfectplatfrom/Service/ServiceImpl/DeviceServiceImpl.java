package com.example.disinfectplatfrom.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.disinfectplatfrom.Mapper.DeviceMapper;
import com.example.disinfectplatfrom.Mapper.OrgnizationMapper;
import com.example.disinfectplatfrom.Mapper.ProjectMapper;
import com.example.disinfectplatfrom.Pojo.Device;
import com.example.disinfectplatfrom.Pojo.Orgnization;
import com.example.disinfectplatfrom.Pojo.Project;
import com.example.disinfectplatfrom.Pojo.User;
import com.example.disinfectplatfrom.Service.DeviceService;
import com.example.disinfectplatfrom.Service.ProjectService;
import com.sun.security.auth.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Autowired
    OrgnizationMapper orgnizationMapper;


    /*
     * @title :ListDevice
     * @Author :Lin
     * @Description :  根据登录账号角色的不同获取设备信息   仅限 HW or 项目管理员 or 组织管理员
     * HW：所有设备信息   项目管理员：所管理项目下的所有组织的设备信息  组织管理员：所管理的组织下的设备信息
     * @Date :23:30 2023/8/29
     * @Param :[]
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.Device>
     **/
    @Override
    @PreAuthorize("hasRole('OA') or hasRole('HW') or hasRole('PA')")
    public Collection<Device> ListDevice(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        if (!ObjectUtils.isEmpty(user)){
            //HW：所有设备信息
            if (user.isHW()){
                LambdaQueryWrapper<Device> lqw = new LambdaQueryWrapper<Device>();
                List<Device> devices = deviceMapper.selectList(lqw);
                return devices;
            }

            //项目管理员：所管理项目下的所有组织的设备信息
            if (user.isPA()){
                //先根据userid查出该用户是哪个项目的管理员
                LambdaQueryWrapper<Project> lqw = new LambdaQueryWrapper<Project>();
                lqw.eq(Project::getAdminId,user.getId());
                Project project = projectMapper.selectOne(lqw);
                Collection<Device> devices = ListDeviceByProjectId(project.getProjectId());
                return devices;
            }

            //组织管理员：所管理的组织下的设备信息
            if (user.isOA()){
                //先根据username查出该用户是哪个组织的管理员
                LambdaQueryWrapper<Orgnization> lqw = new LambdaQueryWrapper<Orgnization>();
                lqw.eq(Orgnization::getAdminAccount,user.getUsername());
                Orgnization orgnization = orgnizationMapper.selectOne(lqw);
                Collection<Device> devices = ListDeviceByOrignizationId(orgnization.getId());

                return devices;
            }
        }
        throw new RuntimeException("用户不存在,请先登录");
    }

    /*
     * @title :ListDeviceByOrignizationId
     * @Author :Lin
     * @Description : 获得组织下所有设备   仅限组织管理员
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
     * @Description :  获得项目下所有设备   仅限项目管理员
     * @Date :13:58 2023/7/15
     * @Param :[projectid]
     * @return :java.util.Collection<com.example.disinfectplatfrom.Pojo.Device>
     **/

    @PreAuthorize("hasRole('OA')")
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
    @PreAuthorize("hasRole('OA') ")
    @Override
    public void AddDevice(Device device,Integer orgnizationid)
    {

        if (!ObjectUtils.isEmpty(device) && !ObjectUtils.isEmpty(orgnizationid)){
            deviceMapper.insert(device);
            deviceMapper.AddOrgnization_Device(orgnizationid,device.getId());
        }

    }

    /*
     * @title :实例初始值设定项
     * @Author :Lin
     * @Description : 批量添加设备   仅组织管理员
     * @Date :0:16 2023/8/30
     * @Param :
     * @return :
     **/
    @PreAuthorize("hasRole('OA') ")
    @Override
    public void AddDevices(List<Device> devices, Integer orgnizationid) {
        if (!ObjectUtils.isEmpty(devices) && !ObjectUtils.isEmpty(orgnizationid)){
            for (Device device : devices) {
                deviceMapper.insert(device);
                deviceMapper.AddOrgnization_Device(orgnizationid,device.getId());
            }
        }
    }
}
