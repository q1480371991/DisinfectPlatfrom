package com.example.disinfectplatfrom.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.disinfectplatfrom.Pojo.Device;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author : Lin
 * @version : [v1.0]
 * @className : DeviceMapper
 * @description : 描述说明该类的功能
 * @createTime : 2023/3/16 20:30
 * @updateUser : Lin
 * @updateTime : 2023/3/16 20:30
 * @updateRemark : 描述说明本次修改内容
 */
@Repository
@Mapper
public interface DeviceMapper extends BaseMapper<Device> {
    @Select("SELECT * FROM device as d,orgnization_device AS od,orgnization AS o WHERE d.id=od.device_id and o.id=od.orgnization_id AND o.id=#{deviceid}")
    public Collection<Device> ListDeviceByOrignizationId(Integer deviceid);

    @Insert("INSERT INTO orgnization_device VALUES(NULL,#{orgnizationid},#{deviceid})")
    public void AddOrgnization_Device(Integer orgnizationid,Integer deviceid);

    @Select("SELECT orgnization_id from orgnization_device where device_id=#{deviceid}")
    public Integer SelectOrgnizationId(Integer deviceid);
}
