package com.example.disinfectplatfrom.Pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
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
public class Project {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String projectname;
    private int adminid;
    private String creattime;
    private String updatetime;
    private String remark;
    private String originaccount;
    private int c_account;
    private int max_account;
    private int delflag;
}
