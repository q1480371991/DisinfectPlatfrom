package com.example.disinfectplatfrom.Pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author : Lin
 * @version : [v1.0]
 * @className : Project_Role
 * @description : 描述说明该类的功能
 * @createTime : 2023/3/16 11:09
 * @updateUser : Lin
 * @updateTime : 2023/3/16 11:09
 * @updateRemark : 描述说明本次修改内容
 */
@Data
public class Project_Role {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer projectId;
    private Integer roleId;
    private Integer maxAccount;
    private Integer currentAccount;
}
