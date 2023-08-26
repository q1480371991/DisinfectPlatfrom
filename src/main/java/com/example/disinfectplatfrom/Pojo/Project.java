package com.example.disinfectplatfrom.Pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author : Lin
 * @version : [v1.0]
 * @className : test
 * @description : 描述说明该类的功能
 * @createTime : 2023/3/1 22:53
 * @updateUser : Lin
 * @updateTime : 2023/3/1 22:53
 * @updateRemark : 描述说明本次修改内容
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer projectId;
    private String projectName;
    private int adminId;
    private Timestamp creatTime;
    @TableField("update_time")
    private Timestamp updateTime;
    private String remark;
    private int originAccountId;
    private int delFlag;

    public Project(Integer projectId, String projectName, String remark) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.remark = remark;
    }
}
