package com.example.disinfectplatfrom.Service;

import com.example.disinfectplatfrom.Pojo.Device;
import com.sun.security.auth.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import java.util.Collection;
import java.util.List;

public interface DeviceService {
    //获得组织下所有设备   仅限？？？
    public Collection<Device> ListDeviceByOrignizationId(Integer deviceid);
    //获得项目下所有设备   仅限项目管理员
    public Collection<Device> ListDeviceByProjectId(Integer projectid);

    //添加设备   仅组织管理员
    public void AddDevice(Device device,Integer orgnizationid);

    //根据登录账号角色的不同获取设备信息   仅限 HW or 项目管理员 or 组织管理员
    public Collection<Device> ListDevice();

    //批量添加设备   仅组织管理员
    public void AddDevices(List<Device> devices, Integer orgnizationid);
}
