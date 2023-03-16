package com.example.disinfectplatfrom.Pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author : Lin
 * @version : [v1.0]
 * @className : Device
 * @description : 描述说明该类的功能
 * @createTime : 2023/3/16 20:26
 * @updateUser : Lin
 * @updateTime : 2023/3/16 20:26
 * @updateRemark : 描述说明本次修改内容
 */
@Data
public class Device {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String deviceName;
    private String locate;
    private Integer status;
    private float duration;
    private Integer cout;
    private Integer temperature;
    private Integer uv;
    private Integer mode;
}
