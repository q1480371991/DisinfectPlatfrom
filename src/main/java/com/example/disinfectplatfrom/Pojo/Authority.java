package com.example.disinfectplatfrom.Pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author : Lin
 * @version : [v1.0]
 * @className : Authority
 * @description : 权限pojo
 * @createTime : 2023/3/10 13:59
 * @updateUser : Lin
 * @updateTime : 2023/3/10 13:59
 * @updateRemark : 描述说明本次修改内容
 */
@Data
public class Authority {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String authorityName;
    private String name;
    private Integer status;
    private Timestamp updateTime;
    private Integer delFlag;
    private String remark;
    
}
