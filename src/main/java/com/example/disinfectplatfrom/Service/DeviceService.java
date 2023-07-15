package com.example.disinfectplatfrom.Service;

import com.example.disinfectplatfrom.Pojo.Device;


import java.util.Collection;

public interface DeviceService {
    //获得组织下所有设备   仅限？？？
    public Collection<Device> ListDeviceByOrignizationId(Integer deviceid);
    //获得项目下所有设备   仅限？？？
    public Collection<Device> ListDeviceByProjectId(Integer projectid);

    //添加设备   仅组织管理员
    public void AddDevice(Device device,Integer orgnizationid);
}
