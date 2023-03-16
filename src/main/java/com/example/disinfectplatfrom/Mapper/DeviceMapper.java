package com.example.disinfectplatfrom.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.disinfectplatfrom.Pojo.Device;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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
}
