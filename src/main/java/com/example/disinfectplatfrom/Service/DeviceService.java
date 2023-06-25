package com.example.disinfectplatfrom.Service;

import com.example.disinfectplatfrom.Pojo.Device;


import java.util.Collection;

public interface DeviceService {
    public Collection<Device> ListDeviceByOrignizationId(Integer deviceid);
    public Collection<Device> ListDeviceByProjectId(Integer projectid);
    public void AddDevice(Device device,Integer orgnizationid);
}
